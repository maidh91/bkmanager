using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Phone.Controls;
using BKmanager.ViewModels;

namespace BKmanager.Views
{
    public partial class Main : PhoneApplicationPage
    {
        private MainViewModel mainViewModel = new MainViewModel();

        public Main()
        {
            InitializeComponent();
            DataContext = mainViewModel;
        }

        #region ApplicationBar events

        private void ApplicationBarIconBack_Click(object sender, EventArgs e)
        {
            mainViewModel.DoBackCommand();
        }

        private void ApplicationBarIconNew_Click(object sender, EventArgs e)
        {
            mainViewModel.DoNewCommand();
        }

        private void ApplicationBarIconNext_Click(object sender, EventArgs e)
        {
            mainViewModel.DoNextCommand();
        }

        private void ApplicationBarRefresh_Click(object sender, EventArgs e)
        {
            mainViewModel.DoRefreshCommand();
        }

        private void ApplicationBarSettings_Click(object sender, EventArgs e)
        {
            mainViewModel.DoSettingsCommand();
        }

        #endregion ApplicationBar events
    }
}