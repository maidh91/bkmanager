using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System;
using BKmanager.Commands;
using BKmanager.Models;
using BKmanager.Services;
using BKmanager.Views;

namespace BKmanager.ViewModels
{
    public class MainViewModel : INotifyPropertyChanged
    {
        #region Properties

        private AccountObject _currentAccount;

        public ObservableCollection<AccountObject> AccountItems { get; set; }
        public ObservableCollection<string> NamHocItems { get; set; }
        public ObservableCollection<int> HocKyItems { get; set; }
        public ObservableCollection<LichThiObject> LichThiItems { get; set; }
        public ObservableCollection<DiemObject> DiemItems { get; set; }
        public ObservableCollection<ThoiKhoaBieuObject> ThoiKhoaBieuItems { get; set; }

        private int _currentNamHocIndex;
        public int CurrentNamHocIndex
        {
            get { return _currentNamHocIndex; }
            set
            {
                _currentNamHocIndex = value;
                NotifyPropertyChanged("CurrentNamHocIndex");

                CurrentNamHoc = NamHocItems[CurrentNamHocIndex];
            }
        }
        private string _currentNamHoc;
        public string CurrentNamHoc
        {
            get { return _currentNamHoc; }
            set { _currentNamHoc = value; NotifyPropertyChanged("CurrentNamHoc"); }
        }

        private int _currentHocKyIndex;
        public int CurrentHocKyIndex
        {
            get { return _currentHocKyIndex; }
            set
            {
                _currentHocKyIndex = value;
                NotifyPropertyChanged("CurrentHocKyIndex");

                CurrentHocKy = HocKyItems[CurrentHocKyIndex];
            }
        }
        private int _currentHocKy;
        public int CurrentHocKy
        {
            get { return _currentHocKy; }
            set { _currentHocKy = value; NotifyPropertyChanged("CurrentHocKy"); }
        }

        private string _currentMSSV;
        public string CurrentMSSV
        {
            get { return _currentMSSV; }
            set { _currentMSSV = value; NotifyPropertyChanged("CurrentMSSV"); }
        }

        private string _currentTen;
        public string CurrentTen
        {
            get { return _currentTen; }
            set { _currentTen = value; NotifyPropertyChanged("CurrentTen"); }
        }

        private int _currentPivotIndex;
        public int CurrentPivotIndex
        {
            get { return _currentPivotIndex; }
            set
            {
                _currentPivotIndex = value;
                NotifyPropertyChanged("CurrentPivotIndex");

                LoadPivotItem();
            }
        }

        #endregion Properties

        public MainViewModel()
        {
            LogService.StatusLog("MainViewModel...");
            try
            {
                Initialize();
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        private void Initialize()
        {
            LogService.StatusLog("Initialize()");
            try
            {
                NamHocChangedCommand = new Command(DoNamHocChangedCommand);
                HocKyChangedCommand = new Command(DoHocKyChangedCommand);

                AccountItems = new ObservableCollection<AccountObject>();
                NamHocItems = new ObservableCollection<string>();
                HocKyItems = new ObservableCollection<int>();
                LichThiItems = new ObservableCollection<LichThiObject>();
                DiemItems = new ObservableCollection<DiemObject>();
                ThoiKhoaBieuItems = new ObservableCollection<ThoiKhoaBieuObject>();

                // load account from db
                AccountObject acc = new AccountObject() { MSSV = "50901524", Ten = "Đinh Hoàng Mai" };
                AccountItems.Add(acc);
                _currentAccount = acc;
                CurrentMSSV = _currentAccount.MSSV;
                CurrentTen = _currentAccount.Ten;

                // load namhoc from db
                NamHocItems.Add("");
                NamHocItems.Add("2011");
                NamHocItems.Add("2012");
                NamHocItems.Add("2013");
                NamHocItems.Add("Tất cả");

                // load hocky
                HocKyItems.Add(1);
                HocKyItems.Add(2);
                HocKyItems.Add(3);
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        #region Commands

        public ICommand NamHocChangedCommand { get; private set; }
        private void DoNamHocChangedCommand()
        {
            LogService.StatusLog("DoNamHocChangedCommand()");
            try
            {
                LoadPivotItem();
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        public ICommand HocKyChangedCommand { get; private set; }
        private void DoHocKyChangedCommand()
        {
            LogService.StatusLog("DoHocKyChangedCommand()");
            try
            {
                LoadPivotItem();
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        #endregion Commands

        #region Commands ApplicationBar

        public void DoNewCommand()
        {
            LogService.StatusLog("DoNewCommand()");
            try
            {
                Popup popup = new Popup();
                popup.Height = 300;
                popup.Width = 400;
                popup.VerticalOffset = 100;
                PopUpUserControl control = new PopUpUserControl();
                popup.Child = control;
                popup.IsOpen = true;

                control.btnCancel.Click += (s, args) => { popup.IsOpen = false; };

                control.btnOK.Click += (s, args) =>
                {
                    popup.IsOpen = false;
                    string mssv = control.tbx.Text;
                    //if(mssv is real)
                    AccountObject acc = new AccountObject() { MSSV = mssv, Ten = "Trần Bích Ngọc" };
                    AccountItems.Add(acc);
                    _currentAccount = acc;
                    CurrentMSSV = _currentAccount.MSSV;
                    CurrentTen = _currentAccount.Ten;
                    LoadPivotItem();
                };
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        public void DoNextCommand()
        {
            LogService.StatusLog("DoNextCommand()");
            try
            {
                int idx = AccountItems.IndexOf(_currentAccount);
                int idxNew = (idx + 1) % (AccountItems.Count);
                _currentAccount = AccountItems[idxNew];
                CurrentMSSV = _currentAccount.MSSV;
                CurrentTen = _currentAccount.Ten;
                LoadPivotItem();
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        public void DoBackCommand()
        {
            LogService.StatusLog("DoBackCommand()");
            try
            {
                int idx = AccountItems.IndexOf(_currentAccount);
                int idxNew = (idx - 1) % (AccountItems.Count);
                _currentAccount = AccountItems[idxNew];
                CurrentMSSV = _currentAccount.MSSV;
                CurrentTen = _currentAccount.Ten;
                LoadPivotItem();
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        public void DoRefreshCommand()
        {
            LogService.StatusLog("DoRefreshCommand()");
            try
            {

            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        public void DoSettingsCommand()
        {
            LogService.StatusLog("DoSettingsCommand()");
            try
            {
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        #endregion Commands ApplicationBar

        #region Helpers

        private void LoadPivotItem()
        {
            LogService.StatusLog("LoadPivotItem()");
            try
            {
                switch (CurrentPivotIndex)
                {
                    case 0: LoadLichThi(); break;
                    case 1: LoadDiem(); break;
                    case 2: LoadThoiKhoaBieu(); break;
                }
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        private void LoadLichThi()
        {
            LogService.StatusLog("LoadLichThi()");
            try
            {
                LichThiItems.Clear();

                // load from db


                if (LichThiItems.Count > 0) return;

                // load from web

                var lichthi = new LichThiObject()
                {
                    TenMon = "Chủ nghĩa Mác-Lênin",
                    MaMon = "001001",
                    Nhom = "A01",
                    SoTC = 5,
                    NgayGK = "17/12/2012",
                    TietGK = 2,
                    PhongGK = "102C2",
                    NgayCK = "31/03/2013",
                    TietCK = 3,
                    PhongCK = "309C1",
                };
                LichThiItems.Add(lichthi);

                lichthi = new LichThiObject()
                {
                    TenMon = "Xác suất thống kê",
                    MaMon = "001002",
                    Nhom = "A03",
                    SoTC = 2,
                    NgayGK = "10/02/2013",
                    TietGK = 2,
                    PhongGK = "102C2",
                    NgayCK = "31/03/2013",
                    TietCK = 3,
                    PhongCK = "309C1",
                };
                LichThiItems.Add(lichthi);

                lichthi = new LichThiObject()
                {
                    TenMon = "Luận văn tốt nghiệp",
                    MaMon = "001003",
                    Nhom = "A02-A",
                    SoTC = 10,
                    NgayGK = "31/03/2013",
                    TietGK = 2,
                    PhongGK = "102C2",
                    NgayCK = "10/02/2013",
                    TietCK = 3,
                    PhongCK = "309C1",
                };
                LichThiItems.Add(lichthi);

                // save db

            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        private void LoadDiem()
        {
            LogService.StatusLog("LoadDiem()");
            try
            {
                DiemItems.Clear();

                var diem = new DiemObject()
                {
                    TenMon = "Tư tưởng HCM",
                    MaMon = "001001",
                    Nhom = "A01",
                    SoTC = 3,
                    DiemGK = 10,
                    DiemCK = 5,
                    DiemTK = 7,
                };
                DiemItems.Add(diem);

                diem = new DiemObject()
                {
                    TenMon = "Giải tích 1",
                    MaMon = "001002",
                    Nhom = "A04",
                    SoTC = 3,
                    DiemGK = 5,
                    DiemCK = 7,
                    DiemTK = 6,
                };
                DiemItems.Add(diem);
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        private void LoadThoiKhoaBieu()
        {
            LogService.StatusLog("LoadThoiKhoaBieu()");
            try
            {
                ThoiKhoaBieuItems.Clear();

                var tkb = new ThoiKhoaBieuObject()
                {
                    TenMon = "Chủ nghĩa Mác-Lênin",
                    MaMon = "001001",
                    Nhom = "A01",
                    SoTC = 5,

                    Thu1 = 2,
                    Tiet1 = 2,
                    Phong1 = "101B1",
                };
                ThoiKhoaBieuItems.Add(tkb);
            }
            catch (Exception ex) { LogService.ErrorLog(ex.Message); }
        }

        #endregion Helpers

        #region INotifyPropertyChanged
        public event PropertyChangedEventHandler PropertyChanged;
        protected void NotifyPropertyChanged(string propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;

            if (handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        #endregion INotifyPropertyChanged
    }
}