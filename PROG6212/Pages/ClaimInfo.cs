using System.Reflection;

namespace st10209811_PROG6212_Part1.Pages
{
    public class ClaimInfo
    {
        
        public int claimID;
        public int amount;
        public string lecturer;
        public string sName;
        public string lecturerID;
        public string status;
        public string notes;

        //Calculates amount
        public double GetAmount(int hrs, double rate)
        {
            double amount = 0;

            amount = hrs * rate;

            return amount;
        }

        //validates hours worked
         public bool VarifyHours(int hours) 
        {
            bool bFlag = true;

            if (hours >= 195)             
                bFlag = false;

            else if (hours <= 0)
                bFlag = false;

            return bFlag;
        }
    }
}
