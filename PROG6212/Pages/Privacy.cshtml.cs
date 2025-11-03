using Azure.Storage.Files.Shares;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Data.SqlClient;
using System.Linq;

namespace st10209811_PROG6212_Part1.Pages
{
    public class PrivacyModel : PageModel
    {
        [BindProperty]
        public IFormFile filename { get; set; }
        public bool UploadSuccess { get; set; }
        public bool UploadError { get; set; }

        private readonly ILogger<PrivacyModel> _logger;
        public int hours;
        
        public double rate;
        public double amount;

        public string notes;
        public string PENDING = "Pending";
        public string name;

        public bool errorHours = true;

        public List<ClaimInfo> claims = new List<ClaimInfo>();

        public PrivacyModel(ILogger<PrivacyModel> logger)
        {
            _logger = logger;
        }

        //Used to display info from the database into a table
        public void OnGet()
        {
            name = Request.Cookies["USER"];
            string myID = Request.Cookies["ID"];

            //Search through the database
            try
            {
                string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";

                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    //Select Querry
                    string sql =
                        "Select CLAIM_ID, AMOUNT, NOTES, STATUS " +
                        "FROM CLAIM WHERE LECTURER_ID = @id;"; 

                    using (SqlCommand command = new SqlCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@id", myID);

                        using (SqlDataReader reader = command.ExecuteReader())
                        {                            
                            //Gets the claim id, amount, notes and status
                            while (reader.Read())
                            {
                                ClaimInfo claimInfo = new ClaimInfo();
                                claimInfo.claimID = reader.GetInt32(0);
                                claimInfo.amount = Convert.ToInt16(reader.GetDouble(1));
                                claimInfo.notes = reader.GetString(2);
                                claimInfo.status = reader.GetString(3);

                                claims.Add(claimInfo);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.ToString());
            }
        }

        //Used to add data into the database based on the values from the form
        public IActionResult OnPost() 
        {
            string myID = Request.Cookies["ID"];

            hours = Convert.ToInt32(Request.Form["hoursworked"]);
            rate = Convert.ToDouble(Request.Form["hourlyRate"]);
            notes = Request.Form["notes"];

            ClaimInfo c = new ClaimInfo();

            //Verify Hours
            if ((c.VarifyHours(hours) == true) && (rate > 0))
            {
                amount = c.GetAmount(hours, rate);

                //Search through database
                try
                {
                    string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";
                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        //Insert Querry
                        connection.Open();
                        string sql = "INSERT INTO claim " +
                            "(AMOUNT, NOTES, LECTURER_ID, STATUS) VALUES " +
                            "(@amount, @notes, @lecturer_id, @status);";

                        //Parse variables into the sql command
                        using (SqlCommand command = new SqlCommand(sql, connection))
                        {
                            command.Parameters.AddWithValue("@amount", amount);
                            command.Parameters.AddWithValue("@notes", notes);
                            command.Parameters.AddWithValue("@lecturer_id", myID);
                            command.Parameters.AddWithValue("@status", PENDING);

                            command.ExecuteNonQuery();
                        }
                    }


                }
                catch (Exception ex)
                {
                    Console.WriteLine("Exception: " + ex.ToString());
                }
            }
            else
                errorHours = false;

            Console.WriteLine("last " + errorHours);
            return RedirectToPage("/Privacy");
        }

        //Logout button
        public IActionResult OnPostLogout()
        {
            return RedirectToPage("/Index");
        }

    }

    
}

