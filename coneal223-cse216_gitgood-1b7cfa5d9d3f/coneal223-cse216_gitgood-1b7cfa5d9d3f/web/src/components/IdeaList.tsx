import * as React from "react";
import { IdeaRow } from "./IdeaRow";
import Header from "./Header";
import { Document, Page } from 'react-pdf';
 
const URL = "http://localhost:4567";
 
type responseObj = {
  // status: string;
  // message: string;
  data: [
    {
      id: number;
      user_id: number;
      content: string;
      votes: number;
      files: Document;
      comments: [
        { id: number; user_id: number; post_id: number; content: string }
      ];
      // created: string;
    }
  ];
};
 
/**
 * React component for holding list of ideas
 */
export class IdeaList extends React.Component {
  /**
   * IdeaList has a data object, a waiting boolean, and an error string for its state
   */
  state = {
    data: {} as responseObj,
    waiting: true,
    error: "",
  };
 
  /**
   * When component mounts, fetch ideas list from server with GET request
   */
  componentDidMount() {
    fetch(`${URL}/posts`)
      .then((response) => response.json())
      .then((response) => {
        this.setState({
          data: response,
          waiting: false,
          error: "",
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
 
  /**
   * Render content of component
   * @returns TSX output to display
   */
  render() {
    if (this.state.waiting) {
      return <div>Loading...</div>;
    } else if (this.state.error !== "") {
      return <div>{this.state.error}</div>;
    } else {
      return (
        <div>
          <Header />
          {this.state.data.data.map((d) => (
            <IdeaRow
              id={d.id}
              user_id={d.user_id}
              content={d.content}
              votes={d.votes}
              files={d.files}
              comments={d.comments}
              // comments = {[d.comments, d.comments]}
              //comments = {[{d.comments.id, d.comments.user_id, d.comments.post_id, d.comments.content}]}
            />
          ))}
        </div>
      );
    }
  }
}
