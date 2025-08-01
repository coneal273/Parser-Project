import * as React from "react";
import { Link } from "react-router-dom";
// import Authenticated from './Authenticated';

import "./NavBar.css";

/**
 * React component for navigation bar at top of page
 */
export class NavBar extends React.Component {
  /**
   * Render content of component
   * @returns TSX output to display
   */
  render() {
    return (
      <nav className="navbar-container">
        <body>
          <header>
            <img src="./public/thebuzzlogo.png"></img>

            {/* <img src="./public/thebuzzlogo.png"></img> */}
            <nav>
              <li className="logo-container">
                <Link to="/">The Buzz</Link>
              </li>
              {/* <div className="logo-container">
          <h1>The Buzz</h1>
        </div> */}
              {/* <li className="home-container">
                <Link to="/home">Home</Link>
              </li> */}
              <li className="form-container">
                <Link to="/create">Create New Idea</Link>
              </li>
              <li className="profile-container">
                <Link to="/profile">Profile</Link>
              </li>
            </nav>
          </header>
        </body>
      </nav>
    );
  }
}
