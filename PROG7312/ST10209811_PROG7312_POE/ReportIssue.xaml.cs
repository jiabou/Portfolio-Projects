using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace ST10209811_PROG7312_POE
{
    public partial class ReportIssue : Window
    {
        private List<Reports> reports = new List<Reports>();

        private bool lFlag, cFlag, fFlag, dFlag = true;     //Validators
        private string location, category, filePath, file, description;

        //(Kampa Plays, 2023)
        private void btnFileSelect_Click(object sender, RoutedEventArgs e)
        {

            OpenFileDialog fileDialog = new OpenFileDialog();
            bool? success = fileDialog.ShowDialog();

            lblPath.Foreground = Brushes.BlueViolet;

            if (success == true)
            {
                file = fileDialog.SafeFileName;
                filePath = fileDialog.FileName;

                lblPath.Content = file;
                fFlag = true;
            }
        }

        public ReportIssue()
        {
            InitializeComponent();

            //Updates Progressbar based on these interactions
            txtLocation.TextChanged += (s, e) => UpdateProgress();
            cbbCategory.SelectionChanged += (s, e) => UpdateProgress();
            rtxtDetails.TextChanged += (s, e) => UpdateProgress();
            btnFileSelect.Click += (s, e) => UpdateProgress();
        }

        private void UpdateProgress()
        {
            pbCompleted.Value = 0;

            if (!string.IsNullOrWhiteSpace(txtLocation.Text))
            {
                pbCompleted.Value += 25;
                lFlag = true;
            }
            else lFlag = false;

            if (cbbCategory.SelectedIndex > -1)
            {
                pbCompleted.Value += 25;
                cFlag = true;
            }
            else cFlag = false;

            if (rtxtDetails != null && rtxtDetails.Document != null)
            {
                TextRange textRange = new TextRange(rtxtDetails.Document.ContentStart, rtxtDetails.Document.ContentEnd);
                if (!string.IsNullOrWhiteSpace(textRange.Text.Trim()))
                {
                    pbCompleted.Value += 25;
                    dFlag = true;
                }
                else dFlag = false;
            }

            if (fFlag)
            {
                pbCompleted.Value += 25;
            }
        }

        private void btnBack_Click(object sender, RoutedEventArgs e)
        {
            MainWindow m = new MainWindow();  
            m.Show();
            this.Close();
        }

        private void btnSubmit_Click(object sender, RoutedEventArgs e)
        {
            MainWindow m = new MainWindow();

            //On Success
            if (lFlag && cFlag && fFlag && dFlag)
            {
                //Stores data into the report list
                Reports newReport = new Reports();
                newReport.location = location;
                newReport.category = category;
                newReport.filePath = filePath;
                newReport.description = description;
                reports.Add(newReport);

                MessageBox.Show("Report has been successfully submitted", "Success!", MessageBoxButton.OK, MessageBoxImage.Information);
                
                this.Close();
                m.Show();
            }
            //On Fail
            else if (!(lFlag))
            {
                MessageBox.Show("No Location or Invalid Location Entered!", "Warning!", MessageBoxButton.OK, MessageBoxImage.Warning);
                txtLocation.Focus();
            }
            else if (!(cFlag))
            {
                MessageBox.Show("No Category or Invalid Category Entered!", "Warning!", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (!(fFlag))
            {
                MessageBox.Show("No File or Invalid File Submitted!", "Warning!", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
            else if (!(dFlag))
            {
                MessageBox.Show("No Description or Invalid Description Entered!", "Warning!", MessageBoxButton.OK, MessageBoxImage.Warning);
                rtxtDetails.Focus();
            }
        }
    }    
}

/*
                                Reference List

               Kampa Plays. 2023. C# WPF Tutorial #10 - OpenFileDialog (File Picker).
               [video online]. Available at:
               https://www.youtube.com/watch?v=Ks9bzPSx7Vs
               [Accessed on 10 September 2025]

 */
