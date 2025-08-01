import * as React from "react";
import "./IdeaRow.css";
import Header from "./Header";
import { Link } from "react-router-dom";

const URL = "http://localhost:4567";

/**
 * React component for a new idea creation form
 */
export class EditProfile extends React.Component {
  /** CreateForm has a string for content as its state */
  state = {
    id: 2, // pass in existing one somehow
    name: "",
    email: "",
    sexual_orientation: "",
    gender_identity: "",
    note: "",
  };

  /**
   * When the input field is updated, update the state of component to match content
   * @param event Change event from input box
   */
  handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    this.setState({
      // not sure if right
      ...this.state,
      [event.target.name]: value,
    });
  };

  handleSelectChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const value = event.target.value;
    this.setState({
      // not sure if right
      ...this.state,
      [event.target.name]: value,
    });
  };

  //   dropdownChange = (event: React.ChangeEvent<HTMLInputElement>) => {
  //     this.setState({value: event.target.value});
  // };

  /**
   * When form is submitted, send post request to server with idea content
   * @param event Form event from submit button
   */
  handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    //console.log("in handle submit");
    fetch(`${URL}/users/2`, {
      // TODO: change route ^^^^^
      method: "PATCH",
      body: JSON.stringify({
        // content: this.state.content,
        // user_id: this.state.user_id,
        id: this.state.id,
        name: this.state.name,
        // username: "",
        email: this.state.email,
        sexual_orientation: this.state.sexual_orientation,
        gender_identity: this.state.gender_identity,
        note: this.state.note,
        // active: false,
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
        <h2>Edit Profile</h2>
        <form onSubmit={this.handleSubmit}>
          <div>Name: </div>
          <input
            type="text"
            name="name"
            value={this.state.name}
            onChange={this.handleChange}
          ></input>
          <div>Username: </div>
          <input
            type="text"
            name="email"
            value={this.state.email}
            onChange={this.handleChange}
          ></input>
          <div>Sexual Orientation: </div>
          <select
            value={this.state.sexual_orientation}
            name="sexual_orientation"
            onChange={this.handleSelectChange}
          >
            <option value="Straight/Heterosexual">Straight/Heterosexual</option>
            <option value="Lesbian/Gay">Lesbian/Gay</option>
            <option value="Bisexual">Bisexual</option>
            <option value="Queer, pansexual, and/or questioning">
              Queer, pansexual, and/or questioning
            </option>
            <option value="Don't know">Don't know</option>
            <option value="Decline to answer">Decline to answer</option>
          </select>
          <div>Gender Identity: </div>
          <select
            value={this.state.gender_identity}
            name="gender_identity"
            onChange={this.handleSelectChange}
          >
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Transgender man">Transgender man</option>
            <option value="Transgender woman">Transgender woman</option>
            <option value="Genderqueer/gender nonconforming">
              Genderqueer/gender nonconforming
            </option>
            <option value="Decline to answer">Decline to answer</option>
          </select>
          <div>Note:</div>
          <input
            type="text"
            name="note"
            value={this.state.note}
            onChange={this.handleChange}
          ></input>
          <div></div>
          {/* <Link to="/profile">
            <button type="submit" onClick={this.handleSubmit}>Submit</button>
          </Link> */}
          <button type="submit">Submit</button>
          {/* <button type="submit">Submit</button> */}
        </form>
      </div>
    );
  }
}
