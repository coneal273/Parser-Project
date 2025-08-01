import * as React from "react";
import LogoutButton from "./Logout";
const URL = "http://localhost:4567";
import Header from "./Header";
import { Link } from "react-router-dom";

/**
 * React component for holding list of ideas
 */

type responseObj = {
  // status: string;
  // message: string;
  data: {
    id: number;
    name: string;
    email: string;
    sexual_orientation: string;
    gender_identity: string;
    note: string;
  };
};
export class Profile extends React.Component<responseObj> {
  // have to fetch user info
  // check if valid token

  state = {
    data: {} as responseObj,
    waiting: true,
    error: "",
  };
  // store user id
  componentDidMount() {
    console.log(localStorage.getItem("user_id"));
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

  render() {
    if (this.state.waiting) {
      return <div>Loading...</div>;
    } else if (this.state.error !== "") {
      return <div>{this.state.error}</div>;
    } else {
      return (
        <div className="profile-container">
          <Header />
          <h1>Profile Page</h1>
          {/* {this.state.profileObj.profileObj.map((d) => d.name)} */}
          <div>Name: </div>
          <>{this.state.data.data.name}</>
          <div></div>
          <div>Email: </div>
          <div>{this.state.data.data.email}</div>
          {/* <div>Username: </div>
          <>{this.state.data.data.username}</> */}
          <div>Gender Identity: </div>
          {/* <>{this.state.data.data.gender_identity}</> */}
          <div>Female</div>
          <div>Sexual Orientation: </div>
          {/* <>{this.state.data.data.sexual_orientation}</> */}
          <div>Straight/Heterosexual</div>
          <div>Note: </div>
          {/* <div>Hey everyone</div> */}
          <>{this.state.data.data.note}</>
          <div></div>
          <Link to="/users/2">
            <button>Edit Profile</button>
          </Link>
          {/* have to edit number */}
          <LogoutButton />
        </div>
      );
    }
  }
}
