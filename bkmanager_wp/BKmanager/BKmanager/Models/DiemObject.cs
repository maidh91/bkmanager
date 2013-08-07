namespace BKmanager.Models
{
    public class DiemObject
    {
        public string MSSV { get; set; }        // key
        public int NamHoc { get; set; }         // key
        public int HocKy { get; set; }          // key

        public string MaMon { get; set; }       // key
        public string TenMon { get; set; }
        public string Nhom { get; set; }
        public int SoTC { get; set; }

        public double DiemGK { get; set; }
        public double DiemCK { get; set; }
        public double DiemTK { get; set; }

        public string NgayCapNhat { get; set; }
    }
}
