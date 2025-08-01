import * as React from "react";
import "./IdeaRow.css";

const URL = "http://localhost:4567";

/**
 * React component for a new idea creation form
 */
export class CreateComment extends React.Component {
  /** CreateForm has a string for content as its state */
  state = {
    content: "",
  };

  /**
   * When the input field is updated, update the state of component to match content
   * @param event Change event from input box
   */
  handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    this.setState({
      content: event.currentTarget.value,
    });
  };

  /**
   * When form is submitted, send post request to server with idea content
   * @param event Form event from submit button
   */
  handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    fetch(`${URL}/posts/:post_id/comments`, {
      method: "POST",
      body: JSON.stringify({
        content: this.state.content,
      }),
    })
      .then((response) => response.json())
      .then((response) => console.log(response));
  };

  /**
   * Render content of component
   * @returns TSX output to display
   */
  render() {
    return (
      <div id="new-idea-container">
        <form onSubmit={this.handleSubmit}>
          <input
            type="text"
            value={this.state.content}
            onChange={this.handleChange}
          ></input>
          <button type="submit">Submit</button>
        </form>
      </div>
    );
  }
}
