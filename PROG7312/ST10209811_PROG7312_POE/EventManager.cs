using System;
using System.Collections.Generic;
using System.Linq;
using ST10209811_PROG7312_POE.Models;

namespace ST10209811_PROG7312_POE
{
    public class EventManager
    {
        // Data structures
        private Stack<EventItem> recentStack = new();                      // Tracks recently added events
        private SortedDictionary<DateTime, EventItem> events = new();      // Stores all events sorted by date
        private HashSet<string> categories = new();                        // Unique event categories

        // Track user search patterns for recommendations
        private Dictionary<string, int> categorySearchCount = new();

        public EventManager()
        {
            InitializeEvents();
        }

        // Get all events sorted by date
        public IEnumerable<EventItem> GetAllEvents()
        {
            return events.Values.OrderBy(e => e.Date);
        }

        // Get all unique categories
        public IEnumerable<string> GetCategories()
        {
            return categories.OrderBy(c => c);
        }

        // Add new event
        public void AddEvent(string name, string category, DateTime date, string location, string description)
        {
            EventItem newEvent = new()
            {
                Name = name,
                Category = category,
                Date = date.ToString("yyyy-MM-dd"),
                Location = location,
                Description = description
            };

            events[date] = newEvent;
            recentStack.Push(newEvent);
            categories.Add(category);
        }

        // Search events by category and date       (Coelho, ND)
        public List<EventItem> SearchEvents(string category, DateTime from, DateTime to)
        {
            if (!string.IsNullOrEmpty(category))
            {
                if (!categorySearchCount.ContainsKey(category))
                    categorySearchCount[category] = 0;
                categorySearchCount[category]++;
            }

            return events.Values.Where(ev =>
                (string.IsNullOrEmpty(category) || ev.Category == category) &&
                DateTime.Parse(ev.Date) >= from &&
                DateTime.Parse(ev.Date) <= to).ToList();
        }

        // Get recently added events
        public List<EventItem> GetRecentEvents()
        {
            return recentStack.ToList();
        }

        // Get recommended events based on search history   (Coelho, ND)
        public List<EventItem> GetRecommendedEvents()
        {
            if (categorySearchCount.Count == 0)
                return new List<EventItem>();

            string topCategory = categorySearchCount.OrderByDescending(c => c.Value).First().Key;

            return events.Values
                         .Where(e => e.Category == topCategory && DateTime.Parse(e.Date) >= DateTime.Now)
                         .OrderBy(e => e.Date)
                         .ToList();
        }

        // Initialize sample events
        private void InitializeEvents()
        {
            AddEvent("Farmers Market", "Community", DateTime.Now.AddDays(3), "Greenfield Park", "Local produce and crafts.");
            AddEvent("Solar Energy Expo", "Renewable Energy", DateTime.Now.AddDays(10), "City Expo Hall", "Learn about solar technologies.");
            AddEvent("Wind Power Seminar", "Education", DateTime.Now.AddDays(15), "Eco Center", "Talks on wind energy projects.");
            AddEvent("Community Cleanup", "Community", DateTime.Now.AddDays(5), "Main Street", "Neighborhood volunteer cleanup.");
            AddEvent("AgriTech Summit", "Technology", DateTime.Now.AddDays(20), "Convention Center", "Modern technology in agriculture.");
            AddEvent("Community Braai", "Community", DateTime.Now.AddDays(7), "Greenfield Park", "Neighborhood bring and braai.");
            AddEvent("Charity Fun Run", "Fundraiser", DateTime.Now.AddDays(1), "Menlyn Park", "Participate in a fun run to raise money for charity.");
            AddEvent("Green Farming Expo", "Education", DateTime.Now.AddDays(30), "Convention Center", "Learn about up-to-date green farming.");
            AddEvent("Pet Donation Drive", "Fundraiser", DateTime.Now.AddDays(23), "Centurion Hall", "Join the drive and help get donations for pets in need.");
            AddEvent("Hydro Energy Expo", "Renewable Energy", DateTime.Now.AddDays(11), "City Expo Hall", "Learn about hydro technologies.");
        }
    }
}


/*
                       Reference List
    Microsoft. 2025. SortedDictionary<TKey,TValue> Class.
    [Online]. Available at:
    https://learn.microsoft.com/en-us/dotnet/api/system.collections.generic.sorteddictionary-2?view=net-9.0
    [Accessed on 15 October 2025]

    Smith, T. 2024. C# Tutorial For Beginners 2024 - 21. HashSet.
    [video online]. Available at:
    https://www.youtube.com/watch?v=XkRke_5_TFs
    [Accessed on 15 October 2025]

    Geekforgeeks. 2025. C# Stack with Examples.
    [Online]. Available at:
    https://www.geeksforgeeks.org/c-sharp/c-sharp-stack-with-examples/
    [Accessed on 15 October 2025]

    Coelho, J. ND. A Simple Recommender System in C#.
    [Online]. Available at:
    https://www.buenadigital.com/blog/a-simple-recommender-system-in-c/
    [Accessed on 15 October 2025]
*/