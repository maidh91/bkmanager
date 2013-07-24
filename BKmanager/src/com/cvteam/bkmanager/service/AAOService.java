package com.cvteam.bkmanager.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import com.cvteam.bkmanager.MainActivity;
import com.cvteam.bkmanager.model.DI__Diem;
import com.cvteam.bkmanager.model.DI__LichThi;
import com.cvteam.bkmanager.model.DI__NienHoc;
import com.cvteam.bkmanager.model.DI__ThoiKhoaBieu;

public class AAOService {

	static class getThread implements Runnable {
		private String url;
		private String result;
		private boolean[] lock;

		public getThread(String url, boolean[] lock) {
			this.url = url;
			this.lock = lock;
			this.result = "";
		}

		public String getResult() {
			return result;
		}

		public void run() {
			try {
				URL siteUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) siteUrl
						.openConnection();
				conn.setRequestMethod("GET");
				// TODO just added
				conn.setConnectTimeout(3500);
				HttpURLConnection.setFollowRedirects(true);
				// allow both GZip and Deflate (ZLib) encodings
				conn.setRequestProperty("Accept-Encoding", "gzip, deflate");

				String encoding = conn.getContentEncoding();

				InputStream inStr = null;
				BufferedReader rd;
				String line;
				// create the appropriate stream wrapper based on
				// the encoding type
				if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
					inStr = new GZIPInputStream(conn.getInputStream());
				} else if (encoding != null
						&& encoding.equalsIgnoreCase("deflate")) {
					inStr = new InflaterInputStream(conn.getInputStream(),
							new Inflater(true));
				} else {
					inStr = conn.getInputStream();
				}
				rd = new BufferedReader(new InputStreamReader(inStr));
				while ((line = rd.readLine()) != null) {
					result += line;
				}
				rd.close();
			} catch (UnknownHostException uhe) {
				result = "404";
				uhe.printStackTrace();
			} catch (java.net.SocketTimeoutException ste) {
				result = "404";
				ste.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// unlock this thread
				lock[0] = true;
			}
		}

	}

	static class postThread implements Runnable {
		private String url;
		private String result;
		private Map<String, String> data;
		private boolean[] lock;

		public postThread(String url, Map<String, String> data, boolean[] lock) {
			this.url = url;
			this.data = data;
			this.lock = lock;
			this.result = "";
		}

		public String getResult() {
			return result;
		}

		public void run() {
			try {
				URL siteUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) siteUrl
						.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// TODO just added
				conn.setConnectTimeout(3500);

				DataOutputStream out = new DataOutputStream(
						conn.getOutputStream());

				Set<String> keys = data.keySet();
				Iterator<String> keyIter = keys.iterator();
				String content = "";
				for (int i = 0; keyIter.hasNext(); i++) {
					Object key = keyIter.next();
					if (i != 0) {
						content += "&";
					}
					content += key + "="
							+ URLEncoder.encode(data.get(key), "UTF-8");
				}
				out.writeBytes(content);
				out.flush();
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = "";
				while ((line = in.readLine()) != null) {
					result += line;
				}
				in.close();
			} catch (UnknownHostException uhe) {
				result = "404";
				uhe.printStackTrace();
			} catch (java.net.SocketTimeoutException ste) {
				result = "404";
				ste.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// unlock this thread
				lock[0] = true;
			}
		}

	}

	/**
	 * 
	 * @param url
	 *            : url
	 * @param data
	 *            : post data
	 * @return: post response
	 */
	private static String doSubmitPost(String url, Map<String, String> data) {
		// lock this thread
		boolean[] lock = { false };

		// run thread for POST method
		postThread pTh = new postThread(url, data, lock);
		new Thread(pTh).start();

		// wait until this thread is unlocked
		while (lock[0] == false)
			;

		return pTh.getResult();
	}

	/**
	 * 
	 * @param url
	 *            : url
	 * @return: GET response
	 */
	private static String doSubmitGet(String url) {
		// lock this thread
		boolean[] lock = { false };

		// run thread for POST method
		getThread pTh = new getThread(url, lock);
		new Thread(pTh).start();

		// wait until this thread is unlocked
		while (lock[0] == false)
			;

		return pTh.getResult();
	}

	/**
	 * 
	 * @return list of available DI__NienHoc from AAO
	 */
	public static List<DI__NienHoc> refreshListNienHoc() throws Exception {
		List<DI__NienHoc> lstHK = new ArrayList<DI__NienHoc>();

		String getResult = doSubmitGet("http://www.aao.hcmut.edu.vn/php/aao_lt.php");

		// check connection
		if (getResult.length() >= 3)
			if (getResult.substring(0, 3).equals("404"))
				throw new UnknownHostException();

		String optionTag = "<option value=";
		int tagPos = getResult.indexOf(optionTag);

		while (tagPos != -1) {
			int _namhoc = 0;
			int _hk = 0;

			String hk = getResult.substring(tagPos + optionTag.length(), tagPos
					+ optionTag.length() + 5);

			_namhoc = Integer.parseInt(hk) / 10;
			_hk = Integer.parseInt(hk) % 10;

			DI__NienHoc newHK = new DI__NienHoc(_namhoc, _hk);
			newHK.hk = _hk;
			newHK.namhoc = _namhoc;

			lstHK.add(newHK);

			tagPos = getResult.indexOf(optionTag, tagPos + optionTag.length()
					+ 5);
		}
		MainActivity.nienHocModel.setHKs(lstHK);
		return lstHK;
	}

	/**
	 * 
	 * @param mssv
	 * @param hk
	 *            i.e: 20122
	 * @param objs
	 *            objs[0] is updateDate or error message if result is empty
	 * @return: list of DI__TKB object
	 */
	public static List<DI__ThoiKhoaBieu> getTKB(String mssv, String hk,
			List<String> objs) {
		List<DI__ThoiKhoaBieu> result = new ArrayList<DI__ThoiKhoaBieu>();

		LogService.freeTag("AAOService", "getTKB");
		try {
			// post request
			Map<String, String> m = new HashMap<String, String>();
			if (hk.equals("")) {
				List<DI__NienHoc> lstNienHoc = refreshListNienHoc();
				if (lstNienHoc.size() == 0) {
					objs.clear();
					objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
					return result;
				}
				DI__NienHoc nien = lstNienHoc.get(2);
				hk = Integer.toString(nien.namhoc * 10 + nien.hk);
			}

			LogService.freeTag("AAOService", "getTKB hk " + hk);
			m.put("HOC_KY", hk);
			m.put("mssv", mssv);
			m.put("image", "Xem-->");

			String tmp = doSubmitPost(
					"http://www.aao.hcmut.edu.vn/php/aao_tkb.php?goto=", m);

			if (tmp.equals("")) {
				result.clear();
				objs.clear();
				objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
				return result;
			}

			if (tmp.indexOf("Không tìm thấy thời khóa biểu sinh viên") != -1) {
				result.clear();
				objs.clear();
				objs.add("Không có thông tin cho học kỳ này.");
				return result;
			}

			if (tmp.indexOf("Không tìm thấy mã số sinh viên này trong dữ liệu") != -1) {
				result.clear();
				objs.clear();
				objs.add("Mã số sinh viên không tồn tại.");
				return result;
			}

			// searching keywords
			String keywordS = "color=#0080FF>";
			String keywordBreak = "color=\"#990033\">";
			String keywordE = "</font>";
			String keywordUpdateDate = "t: ";

			int start, end;

			start = 0;
			end = 0;

			// start = tmp.indexOf(keywordS);
			// end = 0;

			String ngayCapNhat;

			// get updated date
			start = tmp.indexOf(keywordUpdateDate, end);
			end = tmp.indexOf(keywordE, start + keywordS.length());
			ngayCapNhat = tmp
					.substring(start + keywordUpdateDate.length(), end);
			objs.add(ngayCapNhat);
			// System.out.print("Ngay cap nhap " + ngayCapNhat);

			start = tmp.indexOf(keywordS, end);

			boolean thisSemesterRead = (start == -1);

			while (thisSemesterRead == false) {

				// System.out.println();
				// get maMH
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String maMH = tmp.substring(start + keywordS.length(), end);
				// System.out.print(maMH + " ");

				// get tenMH
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String tenMH = tmp.substring(start + keywordS.length(), end);
				// System.out.print(tenMH + " ");

				// kiem tra
				start = tmp.indexOf(keywordBreak, end);
				// followed fields will not available
				if (start != -1) {
					// check if there is any column between
					int tmpInt = tmp.indexOf(keywordS, end);
					if ((tmpInt == -1) || (tmpInt > start)) {
						end = tmp.indexOf(keywordE,
								start + keywordBreak.length());
						String notice = tmp.substring(
								start + keywordBreak.length(), end);

						Pattern pattern = Pattern.compile("(Môn.*)");
						Matcher matcher = pattern.matcher(notice);
						if (matcher.find()) {
							notice = matcher.group(0);
						}

						start = tmp.indexOf(keywordS, end);
						thisSemesterRead = (start == -1);

						DI__ThoiKhoaBieu tkb = new DI__ThoiKhoaBieu();
						tkb.mssv = mssv;
						tkb.namhoc = Integer.parseInt(hk) / 10;
						tkb.hocky = Integer.parseInt(hk) % 10;
						tkb.mamh = maMH;
						tkb.tenmh = tenMH;
						tkb.notice = notice;

						result.add(tkb);

						continue;
					}
				}

				// get nhom_to
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String nhom_to = tmp.substring(start + keywordS.length(), end);
				// System.out.print(nhom_to + " ");

				// ngay hoc dau tien
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				int thu1 = 0;

				try {
					thu1 = Integer.parseInt(tmp.substring(
							start + keywordS.length(), end));
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}

				// System.out.print(thu1 + " ");

				// buoi hoc dau tien
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String tiet1 = tmp.substring(start + keywordS.length(), end);
				// System.out.print(tiet1 + " ");

				// phong hoc dau tien
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String phong1 = tmp.substring(start + keywordS.length(), end);
				// System.out.print(phong1 + " ");

				// ngay hoc thu 2
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				int thu2 = 0;

				try {
					thu2 = Integer.parseInt(tmp.substring(
							start + keywordS.length(), end));
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}

				// System.out.print(thu2 + " ");

				// buoi hoc thu 2
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String tiet2 = tmp.substring(start + keywordS.length(), end);
				// System.out.print(tiet2 + " ");

				// phong hoc thu 2
				start = tmp.indexOf(keywordS, end);
				end = tmp.indexOf(keywordE, start + keywordS.length());
				String phong2 = tmp.substring(start + keywordS.length(), end);
				// System.out.print(phong2 + " ");

				start = tmp.indexOf(keywordS, end);

				thisSemesterRead = (start == -1);

				DI__ThoiKhoaBieu tkb = new DI__ThoiKhoaBieu();
				tkb.mssv = mssv;
				tkb.namhoc = Integer.parseInt(hk) / 10;
				tkb.hocky = Integer.parseInt(hk) % 10;
				tkb.mamh = maMH;
				tkb.tenmh = tenMH;
				tkb.nhomto = nhom_to;
				tkb.thu1 = thu1;
				tkb.tiet1 = tiet1;
				tkb.phong1 = phong1;
				tkb.thu2 = thu2;
				tkb.tiet2 = tiet2;
				tkb.phong2 = phong2;

				result.add(tkb);
			}
		} catch (UnknownHostException uhe) {
			result.clear();
			objs.clear();
			objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
		} catch (Exception ex) {
			ex.printStackTrace();
			result.clear();
			objs.clear();
			objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
		}

		if (result.size() == 0 && objs.size() == 0) {
			objs.add("Chưa có thông tin mới!");
		}
		return result;
	}

	/**
    *
    * @param mssv
    * @param hk
    *            : i.e: 20122
    * @param objs
    *            : objs[0] is updateDate or error message if result is empty
    * @return map of DI__LichThi with id
    */
   public static List<DI__LichThi> getLichThi(String mssv, String hk,
           List<String> objs) {

       List<DI__LichThi> result = new ArrayList<DI__LichThi>();

       try {
           // get latest semester
           if (hk.equals("")) {
               List<DI__NienHoc> lstNienHoc = refreshListNienHoc();

               if (lstNienHoc.size() == 0) {
                   objs.clear();
                   objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
                   return result;
               }
               DI__NienHoc nien = lstNienHoc.get(2);
               hk = Integer.toString(nien.namhoc * 10 + nien.hk);
           }

           // get DI__LichThi objects based on mssv and the latest semester
           Map<String, String> m = new HashMap<String, String>();
           m.put("HOC_KY", hk);
           m.put("mssv", mssv);
           m.put("image", "Xem-->");

           String tmp = doSubmitPost(
                   "http://www.aao.hcmut.edu.vn/php/aao_lt.php?goto=", m);

           if (tmp.equals("")) {
               result.clear();
               objs.clear();
               objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
               return result;
           }

           if (tmp.indexOf("Không tìm thấy mã số sinh viên này trong dữ liệu") != -1) {
               result.clear();
               objs.clear();
               objs.add("Mã số sinh viên không tồn tại.");
               return result;
           }

           if (tmp.indexOf("Không tìm thấy") != -1) {
               result.clear();
               objs.clear();
               objs.add("Không có thông tin cho học kỳ này.");
               return result;
           }

           String keywordS = "color=#0080FF>";
           String keywordE = "</font>";

           // get updateDate
           String keywordUpdateDate = "t: ";
           String ngayCapNhat = "";
           int start = tmp.indexOf(keywordUpdateDate);
           ngayCapNhat = tmp.substring(start + keywordUpdateDate.length(),
                   start + keywordUpdateDate.length() + 10);
           objs.add(ngayCapNhat);

           start = tmp.indexOf(keywordS);
           int end = 0;

           // System.out.println(start);

           boolean exit = (start == -1);
           while (exit == false) {
               // get maMH
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String maMH = tmp.substring(start + keywordS.length(), end);
               // System.out.println(maMH + "\t");
               // get tenMH
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String tenMH = tmp.substring(start + keywordS.length(), end);
               // System.out.println(tenMH + "\t");
               // get nhom_to
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String nhom_to = tmp.substring(start + keywordS.length(), end);
               // System.out.println(nhom_to + "\t");

               // get ngayGK
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String nG = tmp.substring(start + keywordS.length(), end);
               // System.out.println(nG + "\t");
               // get tietGK
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               int tG = Integer.parseInt("0"
                       + tmp.substring(start + keywordS.length(), end));
               // System.out.println(tG + "\t");
               // get phongGK
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String pG = tmp.substring(start + keywordS.length(), end);
               // System.out.println(pG);

               // get ngayCK
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String nC = tmp.substring(start + keywordS.length(), end);
               // System.out.println(nC + "\t");
               // get tietGK
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               int tC = Integer.parseInt("0"
                       + tmp.substring(start + keywordS.length(), end));
               // System.out.println(tC + "\t");
               // get phongGK
               start = tmp.indexOf(keywordS, end);
               end = tmp.indexOf(keywordE, start + keywordS.length());
               String pC = tmp.substring(start + keywordS.length(), end);
               // System.out.println(pC + "\t");

               DI__LichThi lt = new DI__LichThi();
               lt.mssv = mssv;
               lt.namhoc = Integer.parseInt(hk) / 10;
               lt.hocky = Integer.parseInt(hk) % 10;
               lt.mamh = maMH;
               lt.tenmh = tenMH;
               lt.nhomto = nhom_to;
               lt.ngaygk = nG;
               lt.tietgk = tG;
               lt.phonggk = pG;
               lt.ngayck = nC;
               lt.tietck = tC;
               lt.phongck = pC;

               result.add(lt);

               start = tmp.indexOf(keywordS, end);
               // System.out.println(start);

               exit = (start == -1);
           }
       } catch (UnknownHostException uhe) {
           result.clear();
           objs.clear();
           objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
       } catch (Exception ex) {
           ex.printStackTrace();
           result.clear();
           objs.clear();
           objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
       }

       if (result.size() == 0 && objs.size() == 0) {
           objs.add("Chưa có thông tin mới!");
       }

       return result;
   }

   /**
   *
   * @param mssv
   * @param nienhoc
   *            : i.e: 20122
   * @param objs
   *            : objs[0] is updateDate or error message if result is empty,
   *            objs[1..5] are tcdkhk, tctlhk, tongsotc, diemtbhk, diemtbtl
   * @return: list of DI__Diem object
   */
  public static List<DI__Diem> getDiem(String mssv, String nienhoc,
          List<Object> objs) {

      List<DI__Diem> result = new ArrayList<DI__Diem>();

      try {
          // post request
          Map<String, String> m = new HashMap<String, String>();
          if (nienhoc.equals("0") || nienhoc.equals(""))
              m.put("HOC_KY", "d.hk_nh is not NULL");
          else
              m.put("HOC_KY", "d.hk_nh=" + nienhoc);
          m.put("mssv", mssv);
          m.put("image", "Xem-->");
          String tmp = doSubmitPost(
                  "http://www.aao.hcmut.edu.vn/php/aao_bd.php?goto=", m);

          if (tmp.length() >= 3)
              if (tmp.substring(0, 3).equals("404")) {
                  result.clear();
                  objs.clear();
                  objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
                  return result;
              }

          if (tmp.equals("")) {
              result.clear();
              objs.clear();
              objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
              return result;
          }

          if (tmp.indexOf("Không tìm thấy mã số sinh viên này trong dữ liệu") != -1) {
              result.clear();
              objs.clear();
              objs.add("Mã số sinh viên không tồn tại.");
              return result;
          }

          if (tmp.indexOf("Không tìm thấy") != -1) {
              result.clear();
              objs.clear();
              objs.add("Không có thông tin cho học kỳ này.");
              return result;
          }

          // searching keywords
          String keywordS = "color=#0080FF>";
          String keywordE = "</font>";
          String keywordSemester = "M HK";
          String keywordUpdateDate = "t: ";

          int start, end, nextSemester;

          nextSemester = tmp.indexOf(keywordSemester);
          start = nextSemester;
          end = 0;

          // start = tmp.indexOf(keywordS);
          // end = 0;

          boolean allSemesterRead = (nextSemester == -1);

          int hk;
          int startYear;
          String ngayCapNhat = "";

          while (allSemesterRead == false) {
              // get semester
              String strHk = tmp.substring(
                      nextSemester + keywordSemester.length(), nextSemester
                              + keywordSemester.length() + 1);

              hk = Integer.parseInt(strHk);
              String strYear = tmp.substring(
                      nextSemester + keywordSemester.length() + 3,
                      nextSemester + keywordSemester.length() + 7);
              startYear = Integer.parseInt(strYear);
              // hk = (startYear % 100) * 10 + hk;
              strHk = strYear + strHk;

              // get updated date
              start = tmp.indexOf(keywordUpdateDate, end);
              end = tmp.indexOf(keywordE, start + keywordS.length());
              if (ngayCapNhat.equals("")) {
                  ngayCapNhat = tmp.substring(
                          start + keywordUpdateDate.length(), end);
                  objs.add(ngayCapNhat);
              }

              start = tmp.indexOf(keywordS, end);
              nextSemester = tmp.indexOf(keywordSemester, end);

              boolean thisSemesterRead = ((start > nextSemester) && (nextSemester != -1))
                      || (start == -1);

              while (thisSemesterRead == false) {

                  // get maMH
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  String maMH = tmp.substring(start + keywordS.length(), end);

                  // get tenMH
                  start = tmp.indexOf(keywordS, end);
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  String tenMH = tmp
                          .substring(start + keywordS.length(), end);
                  // System.out.println("Mon :" + tenMH);

                  // get nhom_to
                  start = tmp.indexOf(keywordS, end);
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  String nhom_to = tmp.substring(start + keywordS.length(),
                          end);

                  // get so tin chi
                  start = tmp.indexOf(keywordS, end);
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  int soTinChi = Integer.parseInt(tmp.substring(start
                          + keywordS.length(), end));

                  // get diem kiem tra
                  start = tmp.indexOf(keywordS, end);
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  float dKT;
                  try {
                      dKT = Float.parseFloat(tmp.substring(
                              start + keywordS.length(), end));
                  } catch (NumberFormatException nFE) {
                      dKT = -1;
                      nFE.printStackTrace(System.out);
                  }

                  // get diem thi
                  start = tmp.indexOf(keywordS, end);
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  float dThi;
                  try {
                      dThi = Float.parseFloat(tmp.substring(
                              start + keywordS.length(), end));
                  } catch (NumberFormatException nFE) {
                      dThi = -1;
                      nFE.printStackTrace(System.out);
                  }

                  // get diem TK
                  start = tmp.indexOf(keywordS, end);
                  end = tmp.indexOf(keywordE, start + keywordS.length());
                  float dTK;
                  try {
                      dTK = Float.parseFloat(tmp.substring(
                              start + keywordS.length(), end));
                  } catch (NumberFormatException nFE) {
                      dTK = -1;
                      nFE.printStackTrace(System.out);
                  }

                  start = tmp.indexOf(keywordS, end);

                  thisSemesterRead = ((start > nextSemester) && (nextSemester != -1))
                          || (start == -1);

                  // create object
                  DI__Diem nDiem = new DI__Diem();
                  nDiem.mssv = mssv;
                  nDiem.namhoc = startYear;
                  nDiem.hocky = hk;
                  nDiem.mamh = maMH;
                  nDiem.tenmh = tenMH;
                  nDiem.nhomto = nhom_to;
                  nDiem.sotc = soTinChi;
                  nDiem.diemkt = dKT;
                  nDiem.diemthi = dThi;
                  nDiem.diemtk = dTK;

                  // insert object into list
                  result.add(nDiem);
              }
              allSemesterRead = (nextSemester == -1);
          }

          // tcdkhk
          String searchPiece = "Tổng số tín chỉ đăng ký học kỳ :";
          start = tmp.indexOf(searchPiece);
          if (start != -1) {
              end = tmp.indexOf("</td>", start + searchPiece.length());
              objs.add(tmp.substring(start + searchPiece.length(), end));

              // tctlhk
              searchPiece = "Tổng số tín chỉ tích lũy học kỳ:";
              start = tmp.indexOf(searchPiece);
              if (start != -1) {
                  end = tmp.indexOf("</td>", start + searchPiece.length());
                  objs.add(tmp.substring(start + searchPiece.length(), end));

                  // tongsotc
                  searchPiece = "Tổng số tín chỉ :";
                  start = tmp.indexOf(searchPiece);
                  if (start != -1) {
                      end = tmp
                              .indexOf("</td>", start + searchPiece.length());
                      objs.add(tmp.substring(start + searchPiece.length(),
                              end));

                      // diemtbhk
                      searchPiece = "Điểm trung bình học kỳ:";
                      start = tmp.indexOf(searchPiece);
                      if (start != -1) {
                          end = tmp.indexOf("</td>",
                                  start + searchPiece.length());
                          objs.add(tmp.substring(
                                  start + searchPiece.length(), end));

                          // diemtbtl
                          searchPiece = "Điểm trung bình tích lũy:";
                          start = tmp.indexOf(searchPiece);
                          if (start != -1) {
                              end = tmp.indexOf("</td>",
                                      start + searchPiece.length());
                              objs.add(tmp.substring(
                                      start + searchPiece.length(), end));
                          }
                      }
                  }
              }
          }
      } catch (Exception ex) {
          ex.printStackTrace();
          result.clear();
          objs.clear();
          objs.add("Không tìm thấy thông tin.\nVui lòng kiểm tra kết nối Internet và thử lại.");
      }

      if (result.size() == 0 && objs.size() == 0) {
          objs.add("Chưa có thông tin mới!");
      }

      return result;
  }
}
