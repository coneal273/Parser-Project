import * as React from "react";
// import { HashRouter as Route, Switch } from "react-router-dom";
// import { CommentList } from "./CommentList";
// import { Link } from "react-router-dom";
// import { useState } from "react";
import ThumbUpIcon from "@mui/icons-material/ThumbUp";
import ThumbDownAltIcon from "@mui/icons-material/ThumbDownAlt";
import { Link } from "react-router-dom";
import { Document, Page } from 'react-pdf';
 
import "./IdeaRow.css";
 
const URL = "http://localhost:4567";
//MAKE A CONSTANTS FILE AND IMPORT URL
 
/**
 * IdeaRow takes an id, a content string, and a number of likes as its properties
 */
type IdeaRowProps = {
  id: number;
  user_id: number; // how to set to right ID
  content: string;
  votes: number;
  files: Document;
  comments: [
    {
      id: number;
      user_id: number;
      post_id: number;
      content: string;
    }
  ];
};
 
type profileObj = {
  // status: string;
  // message: string;
  data: {
    id: number;
    name: string;
    username: string;
  };
};
/**
 * React component for a singular idea and like button
 */
export class IdeaRow extends React.Component<IdeaRowProps> {
  /**
   * IdeaRow has two values in its state, liked boolean and likes count
   */
  state = {
    liked: false,
    disliked: false,
    likes: this.props.votes,
    content: "",
    user_id: 2, // not sure if this is needed
    // viewComments: false,
    data: {} as profileObj,
  };
 
  componentDidMount() {
    fetch(`${URL}/users/2`) // TODO: get user id and pass that in -- need to get from /login
      .then((response) => response.json())
      .then((response) => {
        this.setState({
          data: response,
          waiting: false,
          error: "",
          // waiting: false,
          // error: "",
        });
        console.log(response);
      })
      .catch((error) =>
        this.setState({
          waiting: false,
          error: "" + error,
        })
      );
  }
 
  // const [viewComments, setViewComments] = useState<boolean>(false);
 
  /**
   * Toggle like button from liked or unliked state when button is pressed
   * @param _e Mouse event from like button
   */
  toggleLike = (_e: React.MouseEvent<HTMLButtonElement>) => {
    var addLike;
    //check if idea is liked already
    if (this.state.liked) {
      //idea is liked
      addLike = false;
      this.setState({
        liked: false,
        likes: this.state.likes,
      });
    } else {
      addLike = true;
      this.setState({
        liked: true,
        disliked: false,
        likes: ++this.state.likes,
      });
    }
    //send update request to server
    fetch(`${URL}/posts/${this.props.id}/likes`, {
      method: "PUT",
      body: JSON.stringify({
        liked: addLike,
      }),
    })
      .then((response) => response.json())
      .then((response) => console.log(response));
  };
 
  toggleDislike = (_e: React.MouseEvent<HTMLButtonElement>) => {
    //var addLike;
    //check if idea is liked already
    if (this.state.disliked) {
      //idea is liked
      //addLike = false;
      this.setState({
        disliked: true,
        likes: this.state.likes,
      });
    } else {
      //idea isn't liked
      //addLike = true;
      this.setState({
        disliked: true,
        liked: false,
        likes: --this.state.likes,
      });
    }
    //send update request to server
    fetch(`${URL}/posts/${this.props.id}/likes`, {
      method: "PUT",
      body: JSON.stringify({
        //liked: addLike,
      }),
    })
      .then((response) => response.json())
      .then((response) => console.log(response));
  };
 
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
    //${URL}
    fetch(`${URL}/posts/${this.props.id}/comments`, {
      // not sure if this is supposed to be local host
      method: "POST",
      body: JSON.stringify({
        content: this.state.content,
        user_id: this.state.user_id,
      }),
    })
      .then((response) => response.json())
      .then((response) => console.log(response));
  };
 
  // editComment = (id, content, event: React.FormEvent<HTMLFormElement>) => {
 
  // }
  /**
   * Render content of component
   * @returns TSX output to display
   */
  render() {
    return (
      <div>
        <div className="idea-row-container">
          <div>Isabella Hudson</div>
          <div className="likes-container">
            <button onClick={this.toggleLike} className="like-button-wrapper">
              <div className="like-button">
                {/* <span>Like</span> */}
                <ThumbUpIcon />
              </div>
            </button>
            <div className="like-button-wrapper">{this.state.likes}</div>
            <button
              onClick={this.toggleDislike}
              className="like-button-wrapper"
            >
              <div className="like-button">
                <ThumbDownAltIcon />
              </div>
            </button>
          </div>
          <div className="idea-row-content">{this.props.content}</div>
          <div className="attachments-title">Attachments:</div>
          <div className="idea-row-attactchments">{this.props.files}</div>
          <div className="comment-title-container">Comments:</div>
          <div className="comment-container">
            {this.props.comments.map((d) => (
              <>
                {" "}
                {/* {d.username} <br /> */}
                {d.content} <br />
                {/* <button onClick={this.editComment(d.id, d.content)}>Edit Comment</button> */}
              </>
            ))}
          </div>
          <form onSubmit={this.handleSubmit}>
            <input
              type="text"
              value={this.state.content}
              onChange={this.handleChange}
            ></input>
            <button type="submit">Submit Comment</button>
          </form>
          <div></div>
        </div>
        <div className="space"></div>
      </div>
    );
  }
}
