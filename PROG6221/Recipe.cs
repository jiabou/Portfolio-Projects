using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ST10209811_PROG6211_POE
{
    class Recipe
    {
        public string name;
        public List<Ingredient> ingredients = new List<Ingredient>();

        public ArrayList steps = new ArrayList();

        //Displays Recipe
        public string DisplayRecipes(Recipe r)
        {
            string s = "";

            s += r.name + "\n";
            s += "-----------------------------------------------------";
            s += "\n";
            foreach (var i in r.ingredients)
            {
                s += "Ingredient: " + i.name + "\n";
                s += "Quantity: " + i.quantity + "\n";
                s += "Measurement: " + i.measurement + "\n";
                s += "Calories: " + i.calories + "\n";
                s += "Food Group: " + i.foodgroup + "\n";
                s += "\n\n";
            }

            s += "-----------------------------------------------------";
            s += "\n";
            s += "Steps: ";
            s += "\n";
            foreach (var step in r.steps)
            {
                int i = 1;
                s += "Step " + i + " :\t" + step;
                s += "\n";
                i++;
            }

            s += "-----------------------------------------------------";
            s += "\n";
            s += "Total calories: " + CalCalo(r);

            return s;
        }

        //Scales ingredients by half x2 or x3
        public double Scale(double v, double m)
        {

            if (m > 1)
                v *= m;
            else
                v /= 2;

            return v;
        }

        //Converts tablespoons <-> cups
        public void Convert(double q, string uom)
        {

            if ((q >= 16) && (uom.Equals("tablespoon")))
            {
                q /= 8;
                uom = "cup";
            }
            else if ((q < 1) && (uom.Equals("cup")))
            {
                q *= 16;
                uom = "tablespoon";
            }

        }

        //Calculates the total Calories
        public double CalCalo(Recipe r)
        {
            double total = 0;

            foreach (var s in r.ingredients)
            {
                total += s.calories;
            }
            return total;
        }
    }
}