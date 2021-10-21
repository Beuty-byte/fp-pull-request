import "./App.css";
import LoginPage from "./components/LoginPage";
import MainPageWithRouter from "./components/MainPage";
import TicketInfo from "./components/TicketInfo";
import TicketDraftPage from "./components/TicketDraftPage";
import TicketCreationPageWithRouter from "./components/TicketCreationPage";
import FeedBackPage from "./components/FeedbackPage";

import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch,
} from "react-router-dom";
import { useState } from "react";

function App() {
  const [isAuthorized, setAuth] = useState(false);

  return (
    <Router>
      <Switch>
        <Route exact path="/">
          {isAuthorized ? <Redirect to="/tickets" /> : <LoginPage authCallback={setAuth} />}
        </Route>
        <Route path="/tickets">
          <MainPageWithRouter />
        </Route>
        <Route path="/create-ticket">
          <TicketCreationPageWithRouter />
        </Route>
        <Route exact path="/feedback/:ticketId">
          <FeedBackPage />
        </Route>
        <Route exact path="/ticket-info/:ticketId">
          <TicketInfo />
        </Route>
        <Route exact path="/tickets-draft/:ticketId">
          <TicketDraftPage />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
