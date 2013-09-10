using System.Collections.Generic;

namespace BKmanager.Models
{
    public class AccountObject
    {
        [SQLite.PrimaryKey]
        public string MSSV { get; set; }    // key
        public string Ten { get; set; }

        public IList<LichThiObject> LichThiItems = new List<LichThiObject>();
        public IList<DiemObject> DiemItems = new List<DiemObject>();
        public IList<ThoiKhoaBieuObject> ThoiKhoaBieuItems = new List<ThoiKhoaBieuObject>();
    }
}
