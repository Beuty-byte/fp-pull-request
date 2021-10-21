import React from "react";
import { Button, TextField, Typography } from "@material-ui/core";

class LoginPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      url: 'http://localhost:8080/login',
      nameValue: "",
      paswordValue: "",
      loginErrors : [],
    };
  }


  handleNameChange = (event) => {
    this.setState({ nameValue: event.target.value });
  };

  handlePasswordChange = (event) => {
    this.setState({ passwordValue: event.target.value });
  };

  handleClickAuth = () => {
  let url = this.state.url;
  let userData = { username: this.state.nameValue, password: this.state.passwordValue};

  
  const request = {
        method: 'POST',
        body: JSON.stringify(userData),
        headers: {
          'Content-Type' : 'application/json;charset=utf-8'
        }
      }
      
      fetch(url, request)
        .then(res => {
            return res.json();
        }).then(data => {
          if(data.token !== undefined){
            localStorage.setItem("token", 'Bearer_' + data.token);
            localStorage.setItem("role", data.userRole);
            this.props.authCallback(true);
          }else{
            this.setState({loginErrors: data});
          }
        });

    // put authorization logic here
    this.props.authCallback(false);
  }
  
  render() {
    const authinticationErrors = this.state.loginErrors;
    return (
      <div className="container">
        <div className="container__title-wrapper">
          <Typography component="h2" variant="h3">
            Login to the Help Desk
          </Typography>
        </div>
        <div className="container__from-wrapper">
          <form>
            <div className="container__inputs-wrapper">
              <div className="form__input-wrapper">
                <TextField
                  onChange={this.handleNameChange}
                  label="User name"
                  variant="outlined"
                  placeholder="User name"
                />
              </div>
              <div className="form__input-wrapper">
                <TextField
                  onChange={this.handlePasswordChange}
                  label="Password"
                  variant="outlined"
                  type="password"
                  placeholder="Password"
                />
              </div>
            </div>
          </form>
        </div>
        <div className="container__button-wrapper">
          <Button
            size="large"
            variant="contained"
            color="primary"
            onClick={this.handleClickAuth}
          >
            Enter
          </Button>
        </div>
{  
        authinticationErrors.length > 0 ?

        <div>
          <ol>
          {authinticationErrors.map((key, value) => {
            return <li>{key} {value}</li>
          })}
          </ol>
        </div>
        :
        ''
  }
      </div>
    );
  }
};

export default LoginPage;
