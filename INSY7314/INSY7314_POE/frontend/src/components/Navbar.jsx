import { Link } from "react-router-dom";

//Dave Gray (2022) Navbar.js:
const Navbar = () => (
  <nav>
    <h1>GlobalBank</h1>
    <Link to="/register">Register</Link> |{" "}
    <Link to="/customer/login">Customer Login</Link> |{" "}
    <Link to="/employee/login">Employee Login</Link>
  </nav>
);

export default Navbar;
/*
Reference list:
React.js App Project | MERN Stack Tutorial. 2022. YouTube video, added by Dave Gray. [Online]. Available at: https://www.youtube.com/watch?v=5cc09qZK0VU [Accessed 9 October 2025]. 
*/