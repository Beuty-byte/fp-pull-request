import React from "react";
import {
  Button,
  InputLabel,
  FormControl,
  MenuItem,
  Select,
  TextField,
  Typography,
} from "@material-ui/core";
import { Link, withRouter, Redirect } from "react-router-dom";
import { ALL_TICKETS } from "../constants/mockTickets";

class TicketCreationPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      url: 'http://localhost:8080/create-tickets',
      CATEGORIES_OPTIONS: [],
      URGENCY_OPTIONS: [],
      categoryValue: 'People Management',
      nameValue: "",
      descriptionValue: " ",
      urgencyValue: "critical",
      resolutionDateValue: "",
      attachmentValue: null,
      commentValue: " ",
      createTicketError: [],
      isRedirect: false,
    };
  }

  componentDidMount() {
    const url = this.state.url;

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    fetch(url, request)
      .then(data => data.json())
      .then(data => {

        let categoriesResult = data.categories.reduce((arr, item) => {

          arr.push({ "label": item.name, "value": item.name })

          return arr;
        }, [])


        let urgencyResult = data.urgencies.reduce((arr, item) => {
          let itemInLowerCase = item.toLowerCase();
          let res = itemInLowerCase.charAt(0).toUpperCase() + itemInLowerCase.slice(1);
          if (item) {
            arr.push({ "label": res, "value": item.toLowerCase() });
          }
          return arr;
        }, []);

        this.setState({
          CATEGORIES_OPTIONS: [...this.state.CATEGORIES_OPTIONS, ...categoriesResult],
          URGENCY_OPTIONS: [...this.state.URGENCY_OPTIONS, ...urgencyResult]
        })
      });


    // set request for getting ticket in draft state

    const ticketFromUrl = this.props.location.pathname.split("/");
    const ticketId = ticketFromUrl[ticketFromUrl.length - 1];
    const ticketData = ALL_TICKETS.find((item) => item.id === +ticketId);

    if (ticketData) {
      this.setState({
        nameValue: ticketData.name,
        resolutionDateValue: ticketData.date,
        commentValue: ticketData.comment,
        descriptionValue: ticketData.description,
        urgencyValue: ticketData.urgency,
        categoryValue: ticketData.category
      });
    }
  }

  handleCategoryChange = (event) => {
    this.setState({
      categoryValue: event.target.value,
    });
  };

  handleNameChange = (event) => {
    this.setState({
      nameValue: event.target.value,
    });
  };

  handleDescriptionChange = (event) => {
    this.setState({
      descriptionValue: event.target.value,
    });
  };

  handleUrgencyChange = (event) => {
    this.setState({
      urgencyValue: event.target.value,
    });
  };

  handleResolutionDate = (event) => {
    this.setState({
      resolutionDateValue: event.target.value,
    });
  };

  handleAttachmentChange = (event) => {
    event.preventDefault();
    this.setState({
      attachmentValue: event.target.files[0]
    });
  }

  handleCommentChange = (event) => {
    this.setState({
      commentValue: event.target.value,
    });
  };

  handleSaveDraft = () => {
    this.handleSubmitTicket("draft");
  };

  handleSubmitTicket = (e) => {
    const {
      url,
      nameValue,
      attachmentValue,
      categoryValue,
      commentValue,
      descriptionValue,
      resolutionDateValue,
      urgencyValue,
    } = this.state;

    let requestUrl = url;

    if (e === "draft") {
      requestUrl = url + "?save=draft";
    }


    let formData = new FormData();
    let jsonBodyData = {
      'name': nameValue,
      'category': categoryValue,
      'comment': commentValue,
      'description': descriptionValue,
      'resolutionDate': resolutionDateValue,
      'urgency': urgencyValue,
    };

    formData.append('jsonBodyData',
      new Blob([JSON.stringify(jsonBodyData)], {
        type: 'application/json',
      }));
    formData.append('uploadFile', this.state.attachmentValue);

    fetch(requestUrl, {
      method: 'POST',
      body: formData,
      headers: { 'Authorization': localStorage.getItem("token") },
    }).then(
      response => {
        if (response.status === 201) {
          this.setState({
            isRedirect: true,
          })

        } else {
          return response.json();
        }
      }
    )
      .then(result => this.setState({ createTicketError: result }))

    // put submit logic here
  };

  render() {
    const {
      nameValue,
      attachmentValue,
      categoryValue,
      commentValue,
      descriptionValue,
      resolutionDateValue,
      urgencyValue,
      createTicketError
    } = this.state;
    if (this.state.isRedirect) {
      return <Redirect to={'/tickets'} />
    }
    return (
      <div className="ticket-creation-form-container">
        <header className="ticket-creation-form-container__navigation-container">
          <Button component={Link} to="/tickets" variant="contained">
            Ticket List
          </Button>
        </header>
        <div className="ticket-creation-form-container__title">
          <Typography display="block" variant="h3">
            Create new ticket
          </Typography>
        </div>
        <div className="ticket-creation-form-container__form">
          <div className="inputs-section">
            <div className="ticket-creation-form-container__inputs-section inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl>
                <TextField
                  required
                  label="Name"
                  variant="outlined"
                  onChange={this.handleNameChange}
                  id="name-label"
                  value={nameValue}
                />
              </FormControl>
            </div>
            <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl variant="outlined" required>
                <InputLabel shrink htmlFor="category-label">
                  Category
                </InputLabel>
                <Select
                  value={categoryValue}
                  label="Category"
                  onChange={this.handleCategoryChange}
                  inputProps={{
                    name: "category",
                    id: "category-label",
                  }}
                >
                  {this.state.CATEGORIES_OPTIONS.map((item, index) => {
                    return (
                      <MenuItem value={item.value} key={index}>
                        {item.label}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </div>
            <div className="inputs-section__ticket-creation-input ticket-creation-input">
              <FormControl variant="outlined" required>
                <InputLabel shrink htmlFor="urgency-label">
                  Urgency
                </InputLabel>
                <Select
                  value={urgencyValue}
                  label="Urgency"
                  onChange={this.handleUrgencyChange}
                  className={"ticket-creation-input_width200"}
                  inputProps={{
                    name: "urgency",
                    id: "urgency-label",
                  }}
                >
                  {this.state.URGENCY_OPTIONS.map((item, index) => {
                    return (
                      <MenuItem value={item.value} key={index}>
                        {item.label}
                      </MenuItem>
                    );
                  })}
                </Select>
              </FormControl>
            </div>
          </div>
          <div className="inputs-section-attachment">
            <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
              <FormControl>
                <InputLabel shrink htmlFor="urgency-label">
                  Desired resolution date
                </InputLabel>
                <TextField
                  onChange={this.handleResolutionDate}
                  label="Desired resolution date"
                  type="date"
                  //      format="DD-MM-YYYY"
                  id="resolution-date"
                  value={resolutionDateValue}
                  InputLabelProps={{
                    shrink: true,
                  }}
                />
              </FormControl>
            </div>
            <div className="ticket-creation-input">
              <FormControl>
                <Typography variant="caption">Add attachment</Typography>
                <TextField
                  type="file"
                  variant="outlined"
                  name="file"
                  onChange={this.handleAttachmentChange}
                />
              </FormControl>
            </div>
          </div>

          <div className="inputs-section">
            <FormControl>
              <TextField
                label="Description"
                multiline
                rows={4}
                variant="outlined"
                value={descriptionValue}
                className="creation-text-field creation-text-field_width680"
                onChange={this.handleDescriptionChange}
              />
            </FormControl>
          </div>
          <div className="inputs-section">
            <FormControl>
              <TextField
                label="Comment"
                multiline
                rows={4}
                variant="outlined"
                value={commentValue}
                className="creation-text-field creation-text-field_width680"
                onChange={this.handleCommentChange}
              />
            </FormControl>
          </div>
          <section className="submit-button-section">
            <Button variant="contained" onClick={this.handleSaveDraft}>
              Save as Draft
            </Button>
            <Button
              variant="contained"
              onClick={this.handleSubmitTicket}
              color="primary"
            >
              Submit
            </Button>
          </section>
        </div>

        {
          createTicketError.length > 0 ?

            <div>
              <ol>
                {createTicketError.map((key, value) => {
                  return <li key={key}>{key} {value}</li>
                })}
              </ol>
            </div>
            :
            ''
        }

      </div>
    );
  }
}

const TicketCreationPageWithRouter = withRouter(TicketCreationPage);
export default TicketCreationPageWithRouter;
