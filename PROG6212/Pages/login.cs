namespace st10209811_PROG6212_Part1.Pages
{
    //Stores Login Details
    public class login
    {
        public bool isLoggedIn { get; set; }
        public string role { get; set; }
        public string name { get; set; }
        public string password { get; set; }

        CookieOptions user;
        public login()
        {
            isLoggedIn = false;
            role = string.Empty;
            name = string.Empty;   
            password = string.Empty;
        }

    }
}
