namespace BKmanager.Models
{
    public class ThoiKhoaBieuObject
    {
        public string MSSV { get; set; }        // key
        public int NamHoc { get; set; }         // key
        public int HocKy { get; set; }          // key

        public string MaMon { get; set; }       // key
        public string TenMon { get; set; }
        public string Nhom { get; set; }
        public int SoTC { get; set; }

        public int Thu1 { get; set; }
        public int Tiet1 { get; set; }
        public string Phong1 { get; set; }
        public int Thu2 { get; set; }
        public int Tiet2 { get; set; }
        public string Phong2 { get; set; }

        public string ThongBao { get; set; }

        public string NgayCapNhat { get; set; }
    }
}
