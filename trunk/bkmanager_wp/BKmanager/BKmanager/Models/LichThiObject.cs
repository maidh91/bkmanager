namespace BKmanager.Models
{
    public class LichThiObject
    {
        public string MSSV { get; set; }        // key
        public int NamHoc { get; set; }         // key
        public int HocKy { get; set; }          // key
        
        public string MaMon { get; set; }       // key
        public string TenMon { get; set; }
        public string Nhom { get; set; }
        public int SoTC { get; set; }

        public string NgayGK { get; set; }
        public int TietGK { get; set; }
        public string PhongGK { get; set; }
        public string NgayCK { get; set; }
        public int TietCK { get; set; }
        public string PhongCK { get; set; }

        public string NgayCapNhat { get; set; }
    }
}
