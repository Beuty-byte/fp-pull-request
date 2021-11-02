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
      allTicketsUrl: 'http://localhost:8080/tickets/all',
      myTicketsUrl: 'http://localhost:8080/tickets/my',
      prop: 42,
      tabValue: 0,
      myTickets: [],
      allTickets: [],
      filteredTickets: [],
      amountTicketsAtPage: 5,
      currentPage: 0,
      totalAmountTickets: 0,

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

    //fetch(myTicketsUrl + '?amountTickets=' + this.state.amountTicketsAtPage, request)
    fetch(this.getCurrentUrl(), request)
      .then(data => data.json())
      .then(data => {
        this.setState({ myTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
      });
  }

  getCurrentUrl = () => {
    if (this.state.tabValue === 0) {
      if (this.state.amountTicketsAtPage !== 5 && this.state.currentPage !== 0) {
        return this.state.myTicketsUrl + '?amountTickets=' + this.state.amountTicketsAtPage + '&page=' + this.state.currentPage;
      } else if (this.state.amountTicketsAtPage !== 5 && this.state.currentPage === 0) {
        return this.state.myTicketsUrl + '?amountTickets=' + this.state.amountTicketsAtPage;
      } else if (this.state.amountTicketsAtPage === 5 && this.state.currentPage !== 0) {
        return this.state.myTicketsUrl + '?page=' + this.state.currentPage;
      }
      return this.state.myTicketsUrl;
    }

    if (this.state.tabValue === 1) {
      if (this.state.amountTicketsAtPage !== 5 && this.state.currentPage !== 0) {
        return this.state.allTicketsUrl + '?amountTickets=' + this.state.amountTicketsAtPage + '&page=' + this.state.currentPage;
      } else if (this.state.amountTicketsAtPage !== 5 && this.state.currentPage === 0) {
        return this.state.allTicketsUrl + '?amountTickets=' + this.state.amountTicketsAtPage;
      } else if (this.state.amountTicketsAtPage === 5 && this.state.currentPage !== 0) {
        return this.state.allTicketsUrl + '?page=' + this.state.currentPage;
      }
      return this.state.allTicketsUrl;
    }
  }

  handleCurrentPage = (value) => {
    console.log(value)
    this.setState({
      currentPage: value
    })

    console.log(this.getCurrentUrl())
    const { tabValue, myTickets, allTickets, allTicketsUrl, myTicketsUrl } = this.state;

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }


    if (tabValue === 0) {
      //  let sortRequestAtMyTicketsUrl = myTicketsUrl + '?amountTickets=' + value ;


      fetch(this.getCurrentUrl(), request)
        .then(data => data.json())
        .then(data => {
          this.setState({ myTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
        });
    }

    if (tabValue === 1) {
      //  let sortRequestAtAllTicketsUrl = allTicketsUrl + '?amountTickets=' + value;

      fetch(this.getCurrentUrl(), request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
        });
    }
  }

  // handlePageChange = () => {
  //   const { allTicketsUrl, myTicketsUrl } = this.state;

  //   const request = {
  //     method: 'GET',
  //     headers: {
  //       'Authorization': localStorage.getItem("token")
  //     }
  //   }

  //   if (value === 0) {
  //     fetch(this.getCurrentUrl, request)
  //       .then(data => data.json())
  //       .then(data => {
  //         this.setState({ myTickets: data.ticketDtoList , totalAmountTickets : data.amountTickets});
  //       });
  //   }

  //   if (value === 1) {
  //     fetch(allTicketsUrl, request)
  //       .then(data => data.json())
  //       .then(data => {
  //         this.setState({ allTickets: data.ticketDtoList , totalAmountTickets : data.amountTickets});
  //       });
  // }



  handleAmountTicketsAtPage = (value) => {
    const { tabValue, myTickets, allTickets, allTicketsUrl, myTicketsUrl } = this.state;

    this.setState({ amountTicketsAtPage: value })

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    if (tabValue === 0) {

      fetch(this.getCurrentUrl(), request)
        .then(data => data.json())
        .then(data => {
          this.setState({ myTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
        });
    }

    if (tabValue === 1) {

      fetch(this.getCurrentUrl(), request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
        });
    }
  }

  handleSortTicket = (value) => {
    const { tabValue, myTickets, allTickets, allTicketsUrl, myTicketsUrl } = this.state;

    const request = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }

    if (tabValue === 0) {

      let sortRequest;

      if (this.getCurrentUrl().includes('?')) {
        sortRequest = this.getCurrentUrl() + '&sort=' + value;
      } else {
        sortRequest = this.getCurrentUrl() + '?sort=' + value;
      }

      fetch(sortRequest, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ myTickets: data.ticketDtoList });
        });
    }

    if (tabValue === 1) {

      let sortRequest;

      if (this.getCurrentUrl().includes('?')) {
        sortRequest = this.getCurrentUrl() + '&sort=' + value;
      } else {
        sortRequest = this.getCurrentUrl() + '?sort=' + value;
      }
      console.log(sortRequest)
      fetch(sortRequest, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
        });
    }
  }

  handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
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
          this.setState({ myTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
        });
    }

    if (value === 1) {
      fetch(allTicketsUrl, request)
        .then(data => data.json())
        .then(data => {
          this.setState({ allTickets: data.ticketDtoList, totalAmountTickets: data.amountTickets });
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
    const filterRequest = { request: event.target.value };

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


      fetch("http://localhost:8080/tickets/filter", request)
        .then(res => {
          return res.json();
        }).then(data => {
          this.setState({ filteredTickets: data, totalAmountTickets: data.amountTickets });
        });
    }
  };

  render() {
    const { allTickets, filteredTickets, myTickets, tabValue, totalAmountTickets } = this.state;
    const { path } = this.props.match;
    const { handleSearchTicket, handleTicketState, handleCurrentPage } = this;

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

              <div>{this.state.totalAmountTickets}</div>
              <div>{this.getCurrentUrl()}</div>
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
                    handlepage={this.handleCurrentPage}
                    AmountTickets={totalAmountTickets}

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
                    handlepage={this.handleCurrentPage}
                    AmountTickets={totalAmountTickets}
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
