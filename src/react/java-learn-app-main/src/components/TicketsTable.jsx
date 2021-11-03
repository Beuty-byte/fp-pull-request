import React from "react";
import PropTypes from "prop-types";
import {
  ButtonGroup,
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
} from "@material-ui/core";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import { TICKETS_TABLE_COLUMNS } from "../constants/tablesColumns";

class TicketsTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      requestUrl : 'http://localhost:8080/tickets/',
      page: 0,
      rowsPerPage: 5,
      currentPage: 0,
    };
  }

  // handleChangePage = (e) => {

  //   console.log(this.state.page)
  //   this.props.handlepage(this.state.page)
  //   console.log("change page");
  // };

  loadPreviousPage = (e) => {
    const p = this.state.page - 1;
     
    this.setState({
      page: p,
    })
    
    this.props.handlepage(p)
  };

  loadNextPage = (e) => {
    const p = this.state.page + 1;

    this.setState({
      page: p,
    })

    this.props.handlepage(p)
  };

  handleChangeRowsPerPage = (event) => {
    this.props.amountTicketsAtPage(event.target.value);
    this.setState({
      rowsPerPage: + event.target.value,
    });

  };

  // handleCancelSubmit = () => {
  //   console.log("Cancel submit");
  // };

  handleSubmitTicket = (e) => {

    const request = {
      method: 'PUT',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }
    const url = this.state.requestUrl + e + '/change-state/' + '?new-state=new';

    fetch(url, request)
      .then(data => {
        if (data.status === 201) {
          this.props.ticketsState({ newStatus: 'NEW', ticketId: e });
        }
      })
  };

  handleCancelTicket = (e) => {
    
    const request = {
      method: 'PUT',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }
    const url = this.state.requestUrl + e + '/change-state/'+ '?new-state=canceled';

    fetch(url, request)
      .then(data => {
        if (data.status === 201) {
          this.props.ticketsState({ newStatus: 'CANCELED', ticketId: e });
        }
      })
  };

  handleApproveTicket = (e) => {
    const request = {
      method: 'PUT',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }
    const url = this.state.requestUrl + e + '/change-state/' + '?new-state=approved';

    fetch(url, request)
      .then(data => {
        if (data.status === 201) {
          this.props.ticketsState({ newStatus: 'APPROVED', ticketId: e });
        }
      })
  };

  handleDeclineTicket = (e) => {
    const request = {
      method: 'PUT',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }
    const url = this.state.requestUrl + e + '/change-state/' + '?new-state=declined';

    fetch(url, request)
      .then(data => {
        if (data.status === 201) {
          this.props.ticketsState({ newStatus: 'DECLINED', ticketId: e });
        }
      })
  };

  handleAssignTicket = (e) => {
    const request = {
      method: 'PUT',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }
    const url = this.state.requestUrl + e + '/change-state/' + '?new-state=in-progress';

    fetch(url, request)
      .then(data => {
        if (data.status === 201) {
          this.props.ticketsState({ newStatus: 'IN_PROGRESS', ticketId: e });
        }
      })
  };

  handleDoneTicket = (e) => {
    const request = {
      method: 'PUT',
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    }
    const url = this.state.requestUrl + e + '/change-state/' + '?new-state=done';

    fetch(url, request)
      .then(data => {
        if (data.status === 201) {
          this.props.ticketsState({ newStatus: 'DONE', ticketId: e });
        }
      })
  };

  render() {
    const { searchCallback, tickets , AmountTickets} = this.props;
    const { page, rowsPerPage } = this.state;
    const { url } = this.props.match;
    const {
      handleChangePage,
      handleChangeRowsPerPage,
      handleSubmitTicket,
      handleApproveTicket,
      handleDeclineTicket,
      handleCancelTicket,
      handleAssignTicket,
      handleDoneTicket
    } = this;
    const userRole = localStorage.getItem("role");

    return (
      <Paper>
        <TableContainer>
          <TextField
            onChange={searchCallback}
            id="filled-full-width"
            label="Search"
            style={{ margin: 5, width: "500px" }}
            placeholder="Search for ticket"
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <Table>
            <TableHead>
              <TableRow>
                <div>{this.state.page}</div>
                <div>{this.state.rowsPerPage}</div>
                {TICKETS_TABLE_COLUMNS.map((column) => (
                  <TableCell align={column.align} key={column.id}>
                    <b>{column.label}</b>
                    {column.label !== 'Action' ?
                      <button onClick={() => this.props.sortTickets(column.label.toLowerCase().replace(/\s+/g, '') + '-asc')}>asc</button>
                      : ''}
                    {column.label !== 'Action' ?
                      <button onClick={() => this.props.sortTickets(column.label.toLowerCase().replace(/\s+/g, '') + '-desc')}>desc</button>
                      : ''}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {tickets
              //  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, index) => {
                  return (
                    <TableRow hover role="checkbox" key={index}>
                      {TICKETS_TABLE_COLUMNS.map((column) => {
                        const value = row[column.id];
                        if (column.id === "name") {
                          return (
                            <TableCell key={column.id}>
                              <Link to={`${url}/${row.id}`}>{value} </Link>

                            </TableCell>
                          );
                        }
                        if (column.id === "action") {


                          if (row.status === "DRAFT" && (userRole === "ROLE_MANAGER" || "ROLE_EMPLOYEE")) {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>

                                <Button
                                  onClick={() => handleCancelTicket(row.id)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Cancel
                                </Button>
                                <Button
                                  onClick={() => handleSubmitTicket(row.id)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Submit
                                </Button>
                              </ButtonGroup>

                            </TableCell>
                          } else if (row.status === "NEW" && userRole === "ROLE_MANAGER" && this.props.tabValue === 1) {

                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>

                                <Button
                                  onClick={() => handleApproveTicket(row.id)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Approve
                                </Button>
                                <Button
                                  onClick={() => handleDeclineTicket(row.id)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Decline
                                </Button>
                                <Button
                                  onClick={() => handleCancelTicket(row.id)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Cancel
                                </Button>
                              </ButtonGroup>

                            </TableCell>

                          } else if (row.status === "APPROVED" && userRole === "ROLE_ENGINEER") {

                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>

                                <Button
                                  onClick={() => handleAssignTicket(row.id)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Assign to me
                                </Button>
                                <Button
                                  onClick={() => handleCancelTicket(row.id)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Cancel
                                </Button>
                              </ButtonGroup>
                            </TableCell>
                          } else if (row.status === "DECLINED" && (userRole === "ROLE_MANAGER" || userRole === "ROLE_EMPLOYEE")) {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>

                                <Button
                                  onClick={() => handleCancelTicket(row.id)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Cancel
                                </Button>
                                <Button
                                  onClick={() => handleSubmitTicket(row.id)}
                                  variant="contained"
                                  color="primary"
                                >
                                  Submit
                                </Button>
                              </ButtonGroup>

                            </TableCell>
                          } else if (row.status === "IN_PROGRESS" && userRole === "ROLE_ENGINEER") {
                            return <TableCell align="center" key={column.id}>
                              <ButtonGroup>

                                <Button
                                  onClick={() => handleDoneTicket(row.id)}
                                  variant="contained"
                                  color="secondary"
                                >
                                  Done
                                </Button>

                              </ButtonGroup>

                            </TableCell>
                          } else if (row.status === "DONE" && this.props.tabValue === 0 && (userRole === "ROLE_MANAGER" || userRole === "ROLE_EMPLOYEE")){
                            return <TableCell align="center" key={column.id}>
                            <ButtonGroup>

                              <Button
                                component={Link}
                                to={`/feedback/${row.id}`}                            
                                variant="contained"
                                color="primary"
                              >
                                CREATE FEEDBACK
                              </Button>

                            </ButtonGroup>

                          </TableCell>
                          }else {
                            return <TableCell key={column.id}></TableCell>
                          }


                        } else {
                          return <TableCell key={column.id}>{value}</TableCell>;
                        }
                      })}
                    </TableRow>
                  );
                })}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 100]}
          component="div"
          //count={tickets.length}
          backIconButtonProps={{
            'aria-label': 'Previous Page',
            'onClick': this.loadPreviousPage,
          }}
          nextIconButtonProps={{
             'aria-label': 'Next Page',
             'onClick': this.loadNextPage,
         }}
          count = {AmountTickets}
          rowsPerPage={rowsPerPage}
          page={page}
          onChangePage={handleChangePage}
          onChangeRowsPerPage={handleChangeRowsPerPage}
        />
      </Paper>
    );
  }
}

TicketsTable.propTypes = {
  searchCallback: PropTypes.func,
  tickets: PropTypes.array,
};

const TicketsTableWithRouter = withRouter(TicketsTable);
export default TicketsTableWithRouter;
