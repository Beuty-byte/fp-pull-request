import React from "react";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import { AppBar, Button, Tab, Tabs } from "@material-ui/core";
import { Link, Switch, Route } from "react-router-dom";
import { withRouter } from "react-router";
import TicketInfoWithRouter from "./TicketInfo";
//import { ALL_TICKETS, MY_TICKETS } from "../constants/mockTickets";

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}
class MainPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      allTicketsUrl: 'http://localhost:8080/all-tickets',
      myTicketsUrl: 'http://localhost:8080/my-tickets',
      prop: 42,
      tabValue: 0,
      myTickets: [],
      allTickets: [],
      filteredTickets: [],
      amountTicketsAtPage: 5,
    };
  }

  componentDidMount() {
    const { myTicketsUrl } = this.state;

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    fetch(myTicketsUrl + '?amountTickets=' + this.state.amountTicketsAtPage, request)
      .then(data => data.json())
      .then(data => {
        this.setState({ myTickets: data });
      });
  }

  handleAmountTicketsAtPage = (value) => {

    const { tabValue, myTickets, allTickets, allTicketsUrl, myTicketsUrl } = this.state;

    this.setState({ amountTicketsAtPage: value });



    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    if (tabValue === 0) {
      let sortRequestAtMyTicketsUrl = myTicketsUrl + '?amountTickets=' + value;

    
      fetch(sortRequestAtMyTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ myTickets: data });
        });
    }

    if (tabValue === 1) {
      let sortRequestAtAllTicketsUrl = allTicketsUrl + '?amountTickets=' + value;
 
      fetch(sortRequestAtAllTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data });
        });
    }
  }

  handleSortTicket = (value) => {
    const { tabValue, myTickets, allTickets, allTicketsUrl, myTicketsUrl } = this.state;

    console.log(value)

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    if (tabValue === 0) {
      let sortRequestAtMyTicketsUrl = myTicketsUrl + '?sort=' + value + '&amountTickets=' + this.state.amountTicketsAtPage;

    
      fetch(sortRequestAtMyTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ myTickets: data });
        });
    }

    if (tabValue === 1) {
      let sortRequestAtAllTicketsUrl = allTicketsUrl + '?sort=' + value + '&amountTickets=' + this.state.amountTicketsAtPage;
  
      fetch(sortRequestAtAllTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data });
        });
    }
  }

  handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    // put logout logic here
    console.log("Logout");
  };

  handleTabChange = (event, value) => {
    const { allTicketsUrl, myTicketsUrl } = this.state;

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    if (value === 0) {
      fetch(myTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ myTickets: data });
        });
    }

    if (value === 1) {
      fetch(allTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data });
        });
    }
    this.setState({
      tabValue: value,
      filteredTickets: []
    });
  };

  handleTicketState = (e) => {

    if (this.state.tabValue === 0) {
      let result = this.state.myTickets;
      result = result.map(todo => {
        if (todo.id === e.ticketId) todo.status = e.newStatus;
        return todo;
      })
      this.setState({ myTickets: result })
    }

    if (this.state.tabValue === 1) {
      let result = this.state.allTickets;
      result = result.map(todo => {
        if (todo.id === e.ticketId) todo.status = e.newStatus;
        return todo;
      })
      this.setState({ allTickets: result })
    }

  }

  handleSearchTicket = (event) => {
    // put search request here
    const filterRequest = { filterRequest: event.target.value };

    const { tabValue, allTickets, myTicketsUrl, filteredTickets } = this.state;

    if (event.target.value.length > 1) {
      const request = {
        method: 'POST',
        body: JSON.stringify(filterRequest),
        headers: {
          'Authorization': localStorage.getItem("token"),
          'Content-Type': 'application/json;charset=utf-8'
        }
      }


      fetch(myTicketsUrl, request)
        .then(res => {
          return res.json();
        }).then(data => {
          this.setState({ filteredTickets: data });
        });


    }
  };

  render() {
    const { allTickets, filteredTickets, myTickets, tabValue } = this.state;
    const { path } = this.props.match;
    const { handleSearchTicket, handleTicketState } = this;

    return (
      <>
        <Switch>
          <Route exact path={path}>
            <div className="buttons-container">
              <Button
                component={Link}
                to="/create-ticket"
                onClick={this.handleCreate}
                variant="contained"
                color="primary"
              >
                Create Ticket
              </Button>
              <Button
                component={Link}
                to="/"
                onClick={this.handleLogout}
                variant="contained"
                color="secondary"
              >
                Logout
              </Button>
            </div>
            <div className="table-container">
              <AppBar position="static">
                <Tabs
                  variant="fullWidth"
                  onChange={this.handleTabChange}
                  value={tabValue}
                >
                  <Tab label="My tickets" {...a11yProps(0)} />
                  <Tab label="All tickets" {...a11yProps(1)} />
                </Tabs>
                <TabPanel value={tabValue} index={0}>
                  <TicketsTable
                    tabValue={tabValue}
                    sortTickets={this.handleSortTicket}
                    searchCallback={handleSearchTicket}
                    ticketsState={handleTicketState}
                    amountTicketsAtPage={this.handleAmountTicketsAtPage}
                    tickets={
                      filteredTickets.length ? filteredTickets : myTickets
                    }
                  />
                </TabPanel>
                <TabPanel value={tabValue} index={1}>
                  <TicketsTable
                    tabValue={tabValue}
                    sortTickets={this.handleSortTicket}
                    searchCallback={handleSearchTicket}
                    ticketsState={handleTicketState}
                    amountTicketsAtPage={this.handleAmountTicketsAtPage}
                    tickets={
                      filteredTickets.length ? filteredTickets : allTickets
                    }
                  />
                </TabPanel>
              </AppBar>
            </div>
          </Route>
          <Route path={`${path}/:ticketId`}>
            <TicketInfoWithRouter />
          </Route>
        </Switch>
      </>
    );
  }
}

const MainPageWithRouter = withRouter(MainPage);
export default MainPageWithRouter;
