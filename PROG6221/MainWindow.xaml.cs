using DocumentFormat.OpenXml.Spreadsheet;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace ST10209811_PROG6211_POE
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        //Declarations
        List<Recipe> recipes = new List<Recipe>();

        Recipe r = new Recipe();
        Recipe temp = new Recipe();

        int iCount = 1;
        int sCount = 1;

        bool iFilter = false;
        bool cFilter = false;
        bool fFilter = false;

        public MainWindow()
        {
            InitializeComponent();

            //Combo box poplulation
            cbbF.Items.Add("Starch");
            cbbF.Items.Add("Fruits and Vegetables");
            cbbF.Items.Add("Fibre");
            cbbF.Items.Add("Meat");
            cbbF.Items.Add("Dairy");
            cbbF.Items.Add("Fats and Oils");
            cbbF.Items.Add("Water");

            cbbM.Items.Add("Cup");
            cbbM.Items.Add("Tablespoon");

            cbbFF.Items.Add("Starch");
            cbbFF.Items.Add("Fruits and Vegetables");
            cbbFF.Items.Add("Fibre");
            cbbFF.Items.Add("Meat");
            cbbFF.Items.Add("Dairy");
            cbbFF.Items.Add("Fats and Oils");
            cbbFF.Items.Add("Water");

            cbbS.Items.Add("x0.5");
            cbbS.Items.Add("x2");
            cbbS.Items.Add("x3");

            cbbFilter.Items.Add("Ingredient");
            cbbFilter.Items.Add("Max Calories");
            cbbFilter.Items.Add("Food Group");
            cbbFF.SelectedIndex = -1;
        }

        //Adds ingredient and displays
        private void btnAddIng_Click(object sender, RoutedEventArgs e)
        {
            string ingredient, measurement, foodGroup;
            double quantity, calories;
            Ingredient i = new Ingredient();

            ingredient = txtI.Text;
            quantity = double.Parse(txtQ.Text);
            measurement = cbbM.Text;
            calories = double.Parse(txtC.Text);
            foodGroup = cbbF.Text;
            
            i.name = ingredient;
            i.quantity = quantity;
            i.measurement = measurement;
            i.calories = calories;
            i.foodgroup = foodGroup;

            //Display ingredient in textbox
            tbI.Text += DisplayIng(ingredient,quantity,measurement,calories,foodGroup);
            iCount++;

            r.Convert(quantity, measurement);
            r.ingredients.Add(i);

            //Clear controls after ingredient entered
            txtI.Clear();
            txtQ.Clear();
            cbbM.SelectedIndex = 0;
            txtC.Clear();
            cbbF.SelectedIndex = 0;
            txtI.Focus();
        }

        //Adds step and displays
        private void btnAddStep_Click(object sender, RoutedEventArgs e)
        {
            string step;

            step = txtS.Text;
            
            //Displays step in textbox
            tbS.Text += DisplayStep(step);
            sCount++;

            r.steps.Add(step);

            //Clear controls and set focus
            txtS.Clear();
            txtS.Focus();
        }

        //Creates recipe and populates list
        private void btnCreate_Click(object sender, RoutedEventArgs e)
        {
            MessageBoxResult result;
            string recipe;

            recipe = txtR.Text;

            r.name = recipe;

            result = MessageBox.Show("Do you want to finish the recipe?", "Finalise Recipe", MessageBoxButton.YesNo);
            if (result == MessageBoxResult.Yes)
            {
                recipes.Add(r);

                MessageBox.Show("Recipe has been created successfully", "Message", MessageBoxButton.OK);

                r = new Recipe();

                //Resets controls
                txtR.Clear();
                txtI.Clear();
                txtQ.Clear();
                cbbM.SelectedIndex = 0;
                txtC.Clear();
                cbbF.SelectedIndex = 0;
                txtS.Clear();

                tbI.Clear();
                tbS.Clear();

                sCount = 1;
                iCount = 1;
            }    

        }

        //Provides info on the 1st tab
        private void btnInfoC_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show("Please enter in the recipe you would like to enter." +
                "\nAdd ingredients to the recipe after fill in the detail by pressing the corresponding button." +
                "\nAdd steps to the recipe after entering a discription by pressing the corresponding button."
                ,"Information",MessageBoxButton.OK, MessageBoxImage.Information);
        }

        //Display image method
        public string DisplayIng(string i,double q, string m, double c, string f)
        {
            string s = "";
            s += "Ingredient " + iCount + ":" + i +"\n";
            s += "-----------------------" + "\n";
            s += "Qauntity: \t" + q + "\n";
            s += "Measurement: \t" + m + "\n";
            s += "Calories: \t\t" + c + "\n";
            s += "Food Group: \t" + f + "\n";
            s += "-----------------------" + "\n";

            return s;
        }

        //display steps method
        public string DisplayStep(string s)
        {
            string step = "";
            step = "Step " + sCount + ": \t" + s + "\n";

            return step;
        }

        //Views all recipes
        private void btnView_Click(object sender, RoutedEventArgs e)
        {
            txtDisplay.Clear();

            foreach (var varRecipe in recipes)
            {
                txtDisplay.Text += varRecipe.DisplayRecipes(varRecipe);
            }
        }

        //Deletes recipe
        private void btnDelete_Click(object sender, RoutedEventArgs e)
        {
            string delete;

            delete = txtDelete.Text;

            foreach (Recipe varRecipe in recipes)
            {
                if (varRecipe.name.Equals(delete))
                {
                    recipes.Remove(varRecipe);

                    foreach (var r in recipes)
                    {
                        txtDisplay.Text += r.DisplayRecipes(varRecipe);
                    }

                    txtDelete.Clear();
                }
            }
        }

        //Displays recipe for scaling
        private void btnSView_Click(object sender, RoutedEventArgs e)
        {
            bool found = false;
            foreach (var varRecipe in recipes)
            {
                if (varRecipe.name.Equals(txtSName.Text))
                {
                    temp = varRecipe;
                    txtScale.Text = varRecipe.DisplayRecipes(varRecipe);

                    txtScale.IsEnabled = false;
                    btnSView.IsEnabled = false;
                    btnScale.IsEnabled = true;
                    btnReset.IsEnabled = true;
                    btnConfirm.IsEnabled = true;
                    found = true;
                    break;
                }
            }
            if (!(found))
            {
                MessageBox.Show("Invalid Recipe name entered!", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                txtSName.Focus();
            }
        }

        //Scales ingredients quantity and displays recipe
        private void btnScale_Click(object sender, RoutedEventArgs e)
        {
            double mult = 0.5;
            
            //Gets multiplier
            switch(cbbS.SelectedIndex)
            {
                case 0: mult = 0.5;
                    break;
                case 1: mult = 2;
                    break;
                case 2: mult = 3;
                    break;
            }

            foreach (var varRecipe in recipes)
            {
                if (varRecipe.name.Equals(txtSName.Text))
                {
                    foreach (var value in varRecipe.ingredients)
                    {
                        value.quantity = varRecipe.Scale(value.quantity, mult);
                        value.calories = varRecipe.Scale(value.calories, mult);
                        varRecipe.Convert(value.quantity, value.measurement);
                    }
                }
            }

            //Displays in textbox
            foreach (var varRecipe in recipes)
            {
                if (varRecipe.name.Equals(txtSName.Text))
                {
                    txtScale.Text = varRecipe.DisplayRecipes(varRecipe);
                }
            }
        }

        //Resets ingredients quantity and displays recipe
        private void btnReset_Click(object sender, RoutedEventArgs e)
        {
            foreach (var varRecipe in recipes)
            {
                if (varRecipe.name.Equals(txtSName.Text))
                {
                    varRecipe.ingredients = temp.ingredients;
                    txtScale.Text = varRecipe.DisplayRecipes(varRecipe);
                }
            }
        }

        //Saves and stores scaled recipe
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            MessageBoxResult result;

            result = MessageBox.Show("Do you want to finish the recipe?", "Finalise Recipe", MessageBoxButton.YesNo);
            if (result == MessageBoxResult.Yes)
            {
                txtScale.Clear();
                txtSName.Clear();
                cbbS.SelectedIndex = 0;
                btnScale.IsEnabled = false;
                btnReset.IsEnabled = false;
                txtScale.IsEnabled = true;
                btnSView.IsEnabled = true;
            }
        }

        //Provides info on the 2nd tab
        private void infoV_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show("View all recipes with the view all button" +
                "\nSelect filter option before filtering recipes ny entering a critera before pressing the corresponding button" +
                "\nDelete a recipe by entering the recipe name and pressing the corresponding button" +
                "\nScale a recipe by entering the recipe name and pressing the corresponding button or reset the scale and view it"
                ,"Information", MessageBoxButton.OK, MessageBoxImage.Information);
        }

        //Filter between ingredient/max calories/
        private void btnFilter_Click(object sender, RoutedEventArgs e)
        {
            txtDisplay.Clear();
            //Checks if combobox is Ingredient
            if (iFilter)
            {
                bool iFound = false;
                string i;

                i = txtFIng.Text;

                foreach (var varRecipe in recipes)
                {
                    foreach (var ingredient in varRecipe.ingredients)
                    {
                        if (ingredient.name.Equals(i))
                        {
                            iFound = true;
                        }
                        else iFound = false;
                    }
                    if (iFound)
                    txtDisplay.Text += varRecipe.DisplayRecipes(varRecipe);
                }
            }
            //Checks if combobox is Max Calories
            else if (cFilter)
            {
                bool cFound = false;
                double c;

                c = Double.Parse(txtC.Text);

                foreach (var varRecipe in recipes)
                {
                    foreach (var ingredient in varRecipe.ingredients)
                    {
                        if (varRecipe.CalCalo(varRecipe) <= c)
                        {
                            cFound = true;
                        }
                        else cFound = false;
                    }
                    if (cFound)
                    txtDisplay.Text += varRecipe.DisplayRecipes(varRecipe);
                }
            }
            //Checks if combobox is Food Group
            else if (fFilter)
            {
                bool fFound = false;
                string f;

                f = cbbFF.Text;

                foreach (var varRecipe in recipes)
                {
                    foreach (var ingredient in varRecipe.ingredients)
                    {
                        if (ingredient.foodgroup.Equals(f))
                        {
                            fFound = true;
                        }
                        else fFound = false;
                    }
                    if (fFound)
                    txtDisplay.Text += varRecipe.DisplayRecipes(varRecipe);
                }
            }

            //resets visiblity of filter controls to be hidden
            lblF.Visibility = Visibility.Collapsed;
            cbbFF.Visibility = Visibility.Collapsed;
            lblC.Visibility = Visibility.Collapsed;
            txtFMaxC.Visibility = Visibility.Collapsed;
            lblI.Visibility = Visibility.Collapsed;
            txtFIng.Visibility = Visibility.Collapsed;
            cbbFilter.SelectedIndex = 0;
            txtFIng.Text = null;
            txtFMaxC.Text = null;
            cbbFF.SelectedIndex = -1;
            btnFilter.Visibility = Visibility.Collapsed;
        }

        //Button to Allow users to see filter option
        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            int option = 0;

            lblF.Visibility = Visibility.Collapsed;
            cbbFF.Visibility = Visibility.Collapsed;
            lblC.Visibility = Visibility.Collapsed;
            txtFMaxC.Visibility = Visibility.Collapsed;
            lblI.Visibility = Visibility.Collapsed;
            txtFIng.Visibility = Visibility.Collapsed;
            btnFilter.Visibility = Visibility.Visible;

            //Displays Lable and textbox based on filter applied
            switch (cbbFilter.SelectedIndex)
            {
                case 0: 
                    lblI.Visibility = Visibility.Visible;
                    txtFIng.Visibility= Visibility.Visible;
                    iFilter = true;
                    break;

                case 1:
                    lblC.Visibility = Visibility.Visible;
                    txtFMaxC.Visibility = Visibility.Visible;
                    cFilter = true;
                    break;

                case 2:
                    lblF.Visibility = Visibility.Visible;
                    cbbFF.Visibility = Visibility.Visible;
                    fFilter = true;
                    break;
            }
        }
    }
}