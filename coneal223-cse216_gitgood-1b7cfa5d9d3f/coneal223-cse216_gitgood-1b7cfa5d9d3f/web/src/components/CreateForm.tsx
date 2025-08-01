import * as React from "react";
import "./IdeaRow.css";
import Header from "./Header";
import { Document, Page } from 'react-pdf';
 
const URL = "http://localhost:4567";
 
/**
 * React component for a new idea creation form
 */
export class CreateForm extends React.Component {
  /** CreateForm has a string for content as its state */
  state = {
    content: "",
    user_id: 2,
    files: "",
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
    // localhost:8080
    fetch(`${URL}/posts`, {
      method: "POST",
      body: JSON.stringify({
        content: this.state.content,
        user_id: this.state.user_id,
        files: this.state.files,
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
        <Header />
        <h2>Create New Post</h2>
        <form onSubmit={this.handleSubmit}>
        <h3>Enter Content</h3>
        <input
          type="text"
          value={this.state.content}
          onChange={this.handleChange}
        />
        <div></div>
        <h3>Add Attachments</h3>
        <input
          type="file"
          value={this.state.files}
          onChange={this.handleChange}
        />
        const encoded: string = Buffer.from(value, 'utf8').toString('base64');
        <div></div>
        <h5>     </h5>
          <button type="submit">Submit</button>
        </form>
      </div>
    );
  }
}
