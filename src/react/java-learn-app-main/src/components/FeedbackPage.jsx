import React from "react";
import { Button, TextField, Typography, FormControl } from "@material-ui/core";
import { Link, withRouter, Redirect } from "react-router-dom";


class FeedbackPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            feedbackValue: null,
            feedbackGradle: null,
            submitError: false,
            isRedirect: false,
            feedbackError: [],
            allFeedbacks: []
        };
    }


    componentDidMount() {

        const ticketFromUrl = window.location.href.split("/");
        const ticketIdFromUrl = ticketFromUrl[ticketFromUrl.length - 1];

        const request = {
            method: 'GET',
            headers: {
                'Authorization': localStorage.getItem("token"),
                //'Content-Type': 'application/json;charset=utf-8'
            }
        }
        const url = 'http://localhost:8080/feedbacks/' + ticketIdFromUrl;

        fetch(url, request)
            .then(data => data.json())
            .then(data => {
                this.setState({ allFeedbacks: data });
            });

    }

    handleChangeFeedback = (event) => {
        this.setState({
            feedbackValue: event.target.value,
        });
    }

    handleGrade = (event) => {
        this.setState({
            feedbackGradle: event,
        });
    }

    handleSubmit = () => {

        const ticketFromUrl = window.location.href.split("/");
        const ticketIdFromUrl = ticketFromUrl[ticketFromUrl.length - 1];

        if (this.state.feedbackGradle !== null) {

            const requestBody = { feedbackComment: this.state.feedbackValue, feedbackGradle: this.state.feedbackGradle }

            const request = {
                method: 'POST',
                body: JSON.stringify(requestBody),
                headers: {
                    'Authorization': localStorage.getItem("token"),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }
            const url = 'http://localhost:8080/feedbacks/' + ticketIdFromUrl;

            fetch(url, request)
                .then(data => {
                    if (data.status === 201) {
                        this.setState({
                            isRedirect: true,
                        })
                    } else {
                        return data.json();
                    }
                })
                .then(result => this.setState({ feedbackError: result }))


        } else {
            this.setState({
                submitError: true,
            })
        }

    }


    render() {
        const { submitError, isRedirect, feedbackError, allFeedbacks } =
            this.state;

        if (isRedirect) {
            return <Redirect to={'/tickets'} />
        }

        return (

            <div className="inputs-section-for-feedback">

                <header className="ticket-creation-form-container__navigation-container">
                    <Button component={Link} to="/tickets" variant="contained">
                        Ticket List
                    </Button>
                </header>

                <FormControl>
                    <div className="feedback-grade">

                        <button onClick={() => this.handleGrade("bad")}>Bad</button>

                        <button onClick={() => this.handleGrade("so-so")}>So-so</button>

                        <button onClick={() => this.handleGrade("normal")}>Normal</button>

                        <button onClick={() => this.handleGrade("good")}>Good</button>

                        <button onClick={() => this.handleGrade("exelent")}>Exelent</button>

                    </div>
                    <TextField
                        label="Feedback"
                        multiline
                        rows={4}
                        variant="outlined"
                        className="creation-text-field creation-text-field_width680"
                        onChange={this.handleChangeFeedback}
                    />
                    <button className="submit-button-from-feedbackpage" onClick={() => this.handleSubmit()}>Submit</button>
                    {submitError ? "You should choose gradle" : ""}

                    {
                        allFeedbacks.length > 0 ?

                            <div>

                                {allFeedbacks.map((key, value) => {

                                    return <div className="allFeedbacks" key={key}>
                                        <div>
                                            Gradle   {key.feedbackGradle}
                                        </div>

                                        <div>
                                            Comment  {key.feedbackComment}
                                        </div>


                                        <div>
                                            Date  {key.localDate}
                                        </div>

                                    </div>
                                })}

                            </div>
                            :
                            ''
                    }
                </FormControl>



                {
                    feedbackError.length > 0 ?

                        <div>
                            <ol>
                                {feedbackError.map((key, value) => {
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
};

export default FeedbackPage;
