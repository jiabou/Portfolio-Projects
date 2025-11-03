using System;
using System.Linq;
using System.Windows;
using ST10209811_PROG7312_POE.Models;

namespace ST10209811_PROG7312_POE
{
    public partial class Events : Window
    {
        private readonly EventManager eventManager;

        //Initializes components and populates lists in UI
        public Events()
        {
            InitializeComponent();
            eventManager = new EventManager();

            LoadCategories();
            DisplayEvents(eventManager.GetAllEvents().ToList());
            DisplayRecentEvents();
            DisplayRecommendations();
        }

        //Displays categories in Combo Box from the Set.
        private void LoadCategories()
        {
            CategoryComboBox.ItemsSource = eventManager.GetCategories().ToList();
            if (CategoryComboBox.Items.Count > 0)
                CategoryComboBox.SelectedIndex = 0;

            FromDatePicker.SelectedDate = DateTime.Now.Date;
            ToDatePicker.SelectedDate = DateTime.Now.Date.AddDays(30);
        }

        //Displayes all events from the sorted dictionary
        private void DisplayEvents(System.Collections.Generic.List<EventItem> events)
        {
            EventsListView.ItemsSource = events;
        }

        //Displayes all events from the sorted dictionary that were added recently.
        private void DisplayRecentEvents()
        {
            RecentEventsListView.ItemsSource = eventManager.GetRecentEvents();
        }

        //Displayes all events from the sorted dictionary based on recommendations.
        private void DisplayRecommendations()
        {
            RecommendationsListView.ItemsSource = eventManager.GetRecommendedEvents();
        }

        //Searches based on Category and From-To dates.
        private void SearchButton_Click(object sender, RoutedEventArgs e)
        {
            string selectedCategory = CategoryComboBox.SelectedItem?.ToString();
            DateTime from = FromDatePicker.SelectedDate ?? DateTime.Now.Date;
            DateTime to = ToDatePicker.SelectedDate ?? DateTime.Now.AddMonths(1).Date;

            
            if (from > to)
            {
                MessageBox.Show("From Date needs to come before the To Date!", "Warning!", MessageBoxButton.OK, MessageBoxImage.Warning
                );
                return; 
            }

            var filtered = eventManager.SearchEvents(selectedCategory, from, to);
            DisplayEvents(filtered);

            // Update recent events
            DisplayRecentEvents();

            // Update recommendations based on search
            DisplayRecommendations();
        }

        //Shows all events from the sorted dictionary.
        private void ShowAllButton_Click(object sender, RoutedEventArgs e)
        {
            DisplayEvents(eventManager.GetAllEvents().ToList());
            DisplayRecentEvents();
            DisplayRecommendations();
        }

        //Goes back to Main Form
        private void BackButton_Click(object sender, RoutedEventArgs e)
        {
            MainWindow mForm = new MainWindow();
            mForm.Show();
            this.Close();
        }
    }
}

/* Reference List
   Fox Learn. 2021. C# Tutorial - Fetch or Filter Data from Date to Date | FoxLearn.
   [video online]. Available at:
   https://www.youtube.com/watch?v=D3MLi08NN1o
   [Accessed on 15 October 2025]
*/
