using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Data;
using System.Data.SqlClient;
using System.Net.NetworkInformation;
using System.Security.Claims;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace st10209811_PROG6212_Part1.Pages
{
    public class HRModel : PageModel
    {
        public List<ClaimInfo> claims = new List<ClaimInfo>();
        public string name;
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
                    "Select claim.CLAIM_ID, lecturer.NAME,lecturer.SURNAME, lecturer.LECTURER_ID, claim.AMOUNT, claim.NOTES " +
                    "FROM CLAIM, LECTURER WHERE claim.LECTURER_ID = lecturer.LECTURER_ID AND claim.STATUS = 'Approved'";                                             

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
                                claimInfo.sName = reader.GetString(2);
                                claimInfo.lecturerID = reader.GetString(3);
                                claimInfo.amount = Convert.ToInt32(reader.GetDouble(4));
                                claimInfo.notes = reader.GetString(5);

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

        public IActionResult OnPostUpdate()
        {
            //Get user input from the Form
            string option = Request.Form["selectOption"];
            string id = Request.Form["updateID"];
            string newDetails = Request.Form["newDetails"];
            string sql = "";

            bool validID = false;

            //Changes update query based on selected field
            if (option.Equals("lName"))
            {
                sql = "Update lecturer SET NAME = @new WHERE LECTURER_ID = @ID";
            }
            else if (option.Equals("lSurname"))
            {
                sql = "Update lecturer SET SURNAME = @new WHERE LECTURER_ID = @ID";
            }
            else if (option.Equals("lEmail"))
            {
                sql = "Update lecturer SET EMAIL = @new WHERE LECTURER_ID = @ID";
            }

            try
            {
                string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";

                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();

                    string sql2 = "Select LECTURER_ID FROM lecturer WHERE LECTURER_ID = @ID";

                    using (SqlCommand command = new SqlCommand(sql2, connection))
                    {
                        //get login of the user
                        command.Parameters.AddWithValue("@ID", id);

                        using (SqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                id = reader.GetString(0);
                                Console.WriteLine(id);
                            }
                        }
                    }
                    validID = true;
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.ToString());
                validID = false;
            }

            Console.WriteLine("SQL" + sql);
            Console.WriteLine("new " + newDetails);
            Console.WriteLine("valid " + validID);
            Console.WriteLine("id " + id);

            //Updates lecturer details (Peled, 2017)
            if (!(newDetails.Equals(null)) && (validID == true))
            try
            {
                string connectionString = "Data Source=labVMH8OX\\SQLEXPRESS;Initial Catalog=prog6212_poe;Integrated Security=True;Connect Timeout=30;Encrypt=False";

                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();

                    using (SqlCommand command = new SqlCommand(sql, connection))
                    {
                        //get login of the user
                        command.Parameters.AddWithValue("@new", newDetails);
                        command.Parameters.AddWithValue("@ID", id);

                        Console.WriteLine("Updated");

                        command.ExecuteNonQuery();
                    }
                        
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.ToString());
            }

            return RedirectToPage("/HR");
        }

        public IActionResult OnPostLogout()
        {
            return RedirectToPage("/Index");
        }
    }
}
