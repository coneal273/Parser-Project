import * as React from "react";
import { HashRouter as Router, Route, Link, Switch } from "react-router-dom";

import { IdeaList } from "./components/IdeaList";
import { CreateForm } from "./components/CreateForm";
import { Profile } from "./components/Profile";
import { EditProfile } from "./components/EditProfile";
// // import { CommentList } from "./components/CommentList";
// import { GoogleLoginPage } from "./components/GoogleLoginPage";
import GoogleLoginPage from "./components/GoogleLoginPage";
// import Authenticated from "./components/Authenticated";
// import { useAuth0 } from "@auth0/auth0-react";

/** App has one property: a number */
type AppProps = { num: number };

export class App extends React.Component<AppProps> {
  /** The global state for this component is a counter */
  state = { num: 0 };

  // const { loginWithPopup, loginWithRedirect, logout, user, isAuthenticated } = useAuth0<TUser>();

  /**
   * When the component mounts, we need to set the initial value of its
   * counter
   */
  componentDidMount = () => {
    this.setState({ num: this.props.num });
  };

  /** Get the current value of the counter */
  getNum = () => this.state.num;

  /** Set the counter value */
  setNum = (num: number) => this.setState({ num });

  // Authenticated();

  /**
   * Render content of component
   * @returns TSX output to display
   */
  render() {
    return (
      <Router>
        <div>
          <Switch>
            <Route exact path="/login" component={GoogleLoginPage} />{" "}
            <Route exact path="/" component={IdeaList} />{" "}
            <Route exact path="/create" component={CreateForm} />
            <Route exact path="/profile" component={Profile} />{" "}
            <Route exact path="/users/2" component={EditProfile} />{" "}
            {/* have to edit number */}
          </Switch>
        </div>
      </Router>
    );
  }
}
