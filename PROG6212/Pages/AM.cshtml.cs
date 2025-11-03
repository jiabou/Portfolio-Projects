using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Data.SqlClient;
using System.Security.Claims;

namespace st10209811_PROG6212_Part1.Pages
{
    public class AMModel : PageModel
    {
        public List<ClaimInfo> claims = new List<ClaimInfo>();
        public string name;

        //Displays database information into a table
        public void OnGet()
        {
            name = Request.Cookies["USER"];
            try
            {
                string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";

                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    //Select Querry
                    connection.Open();
                    string sql =
                        "Select claim.CLAIM_ID, lecturer.NAME, lecturer.LECTURER_ID, claim.AMOUNT, claim.NOTES, claim.STATUS " +
                        "FROM CLAIM, LECTURER" +
                        " WHERE CLAIM.LECTURER_ID = LECTURER.LECTURER_ID";

                    using (SqlCommand command = new SqlCommand(sql, connection))
                    {
                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            //Reads and gets claim information from the database
                            while (reader.Read())
                            {
                                ClaimInfo claimInfo = new ClaimInfo();
                                claimInfo.claimID = reader.GetInt32(0);
                                claimInfo.lecturer = reader.GetString(1);
                                claimInfo.lecturerID = reader.GetString(2);
                                claimInfo.amount = Convert.ToInt32(reader.GetDouble(3));
                                claimInfo.notes = reader.GetString(4);
                                claimInfo.status = reader.GetString(5);

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

        //Approve button method
        public IActionResult OnPostApproveClick()
        {
            string id;
            id = Request.Form["a"];
            try
            {
                string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    //Update Querry
                    connection.Open();
                    string sql = "Update CLAIM SET status = 'Approved' Where CLAIM_ID = @id;";

                    //Sets current claims status to approved
                        using (SqlCommand command = new SqlCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@id", id);

                        command.ExecuteNonQuery();
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.ToString());
            }

            return RedirectToPage("/AM");
        }

        //Reject button method
        public IActionResult OnPostRejectClick()
        {
            string id;
            id = Request.Form["r"];
            try
            {
                string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    //Update Querry
                    connection.Open();
                    string sql = "Update CLAIM SET status = 'Rejected' Where CLAIM_ID = @id;";

                    //Sets current claim status to rejected
                    using (SqlCommand command = new SqlCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@id", id);

                        command.ExecuteNonQuery();
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.ToString());
            }

            return RedirectToPage("/AM");
        }

        //Logout
        public IActionResult OnPostLogout()
        {
            return RedirectToPage("/Index");
        }
    }
}
