using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Data.SqlClient;
using System.Security.Claims;

namespace st10209811_PROG6212_Part1.Pages
{
    public class IndexModel : PageModel
    {        
        
        private readonly ILogger<IndexModel> _logger;
        public bool loginFlag = true;

        public IndexModel(ILogger<IndexModel> logger)
        {
            _logger = logger;
        }

        public void OnGet()
        {

        }

        public IActionResult OnPost()
        {
            CookieOptions user = new CookieOptions();
            user.Expires = DateTime.Now.AddMinutes(10);

            string role = Request.Form["type"];
            string name = Request.Form["uname"];
            string pass = Request.Form["psw"];

            string n = "";
            string u = "";
            string sql = "";

            //check what type of user was selected
            if (role.Equals("l"))
            {
                sql = "SELECT LECTURER_ID,NAME,PASSWORD FROM lecturer WHERE NAME = @name AND PASSWORD = @pass;";
            }
            else if (role.Equals("pc"))
            {
                sql = "SELECT PC_ID,NAME,PASSWORD FROM project_coordinator WHERE NAME = @name AND PASSWORD = @pass;";
            }
            else if (role.Equals("am"))
            {
                sql = "SELECT AC_ID,NAME,PASSWORD FROM academic_manager WHERE NAME = @name AND PASSWORD = @pass;";
            }
            else if (role.Equals("hr"))
            {
                sql = "SELECT HR_ID,NAME,PASSWORD FROM human_resources WHERE NAME = @name AND PASSWORD = @pass;";
            }

                try
                {
                    string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";

                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        connection.Open();

                        using (SqlCommand command = new SqlCommand(sql, connection))
                        {
                            //get login of the user
                            command.Parameters.AddWithValue("@name", name);
                            command.Parameters.AddWithValue("@pass", pass);

                            using (SqlDataReader reader = command.ExecuteReader())
                            {
                                while (reader.Read())
                                {
                                    n = reader.GetString(0);
                                    u = reader.GetString(1);
                                }
                            }
                        }
                    }
                    
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Exception: " + ex.ToString());
                }
            Console.WriteLine(loginFlag);

            //Checks if correct user has been entered
            if (n != "")
            {
                //stores current user login into cookies
                Response.Cookies.Append("ID", n, user);
                Response.Cookies.Append("USER", u, user);

                //Navigates to different pages based on the user type
                if (role.Equals("l"))
                {
                    return RedirectToPage("/Privacy");
                }
                else if (role.Equals("pc"))
                {
                    return RedirectToPage("/PC");
                }
                else if (role.Equals("am"))
                {
                    return RedirectToPage("/AM");
                }
                else if (role.Equals("hr"))
                {
                    return RedirectToPage("/HR");
                }
            }
            else
                loginFlag = false;

            Console.WriteLine(loginFlag);
            return Page();
            

        }
    }
}
