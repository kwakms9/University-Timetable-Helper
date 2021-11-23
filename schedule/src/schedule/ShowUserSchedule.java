package schedule;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Servlet implementation class ShowUserSchedule
 */
@WebServlet("/loadSchedule")
public class ShowUserSchedule extends HttpServlet {
	
    public ShowUserSchedule() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected JSONObject printList(ScheduleBean schedule, String number) {	//시간 중복된 것
			JSONObject info = new JSONObject();
			info.put("div", schedule.getDiv());
			info.put("grade", schedule.getGrade());
			info.put("major", schedule.getMajor());
			info.put("lectureNumber", schedule.getLectureNumber());
			info.put("lecture", schedule.getLecture());
			info.put("credit", schedule.getCredit());
			info.put("time", schedule.getTime());
			info.put("prof", schedule.getProf());
			info.put("lectureTime", schedule.getLectureTime());
			info.put("lectureRoom", schedule.getLectureRoom());
			info.put("part", schedule.getPart());
			info.put("note", schedule.getNote());
			info.put("score", schedule.getScore());
			info.put("link", schedule.getLink());
			info.put("overlapNumber", number);	//시간 중복여부
			return info;
	}
	
	protected JSONObject printList(ScheduleBean schedule) {	//평범
		JSONObject info = new JSONObject();
		info.put("div", schedule.getDiv());
		info.put("grade", schedule.getGrade());
		info.put("major", schedule.getMajor());
		info.put("lectureNumber", schedule.getLectureNumber());
		info.put("lecture", schedule.getLecture());
		info.put("credit", schedule.getCredit());
		info.put("time", schedule.getTime());
		info.put("prof", schedule.getProf());
		info.put("lectureTime", schedule.getLectureTime());
		info.put("lectureRoom", schedule.getLectureRoom());
		info.put("part", schedule.getPart());
		info.put("note", schedule.getNote());
		info.put("score", schedule.getScore());
		info.put("link", schedule.getLink());
		return info;
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		JSONObject data = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject info;
		
		String userId = request.getParameter("userId");
		String loadType = request.getParameter("loadType");
		System.out.println(userId);
		System.out.println(loadType);
		List <UserScheduleBean> userScheduleList;
		BeanFactory factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
		UserScheduleDAO dao = (UserScheduleDAO) factory.getBean("userScheduleDAO");
		factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
		ScheduleDAO sdao = (ScheduleDAO) factory.getBean("scheduleDAO");
		
		userScheduleList =dao.getScheduleList(userId);	//사용자가 저장한 과목목록 가져오기
		
		String query ="";
		for(UserScheduleBean list : userScheduleList) {	//가져온 목록으로 과목 데이터 쿼리문 만들기
			query+="수강번호="+list.getLectureNumber()+" or ";
		}
		if(userScheduleList.size()>0) {
			query = query.substring(0, query.length()-3); //맨뒤의 or 지우기
		}else {
			out.print("noData");
			return;
		}
		System.out.println("query = "+query);
		
		
		List <ScheduleBean> scheduleList;
		scheduleList =sdao.getSchuduleInfo(query);	//수강번호 쿼리
		
		if(loadType.equals("selected")) {	//선택한 과목들 모두 출력
			for(ScheduleBean schedule : scheduleList) {
				array.add(printList(schedule));
			}
			
		}else {
			//중복과목 선별 및 출력
			for(int i =0; i<scheduleList.size()-1;i++){
				String str = scheduleList.get(i).getLectureTime();
				if(str.equals("미지정") || str.equals("본교가상") || str.contains("MOOC")) {//강의시간이 없을경우
					continue;
				}
				str = scheduleList.get(i).getLectureTime().replace("(", "").replace(")", "");//괄호 지우기
				String data1 [] = str.split("\\|");
				String day1 [] = new String[data1.length];/*******나중에 변수량 조절하기,,,,애초에 모든것을 다 분리 리스트저장해서 비교하기는??**********/
				String day2 [];
				
				for(int k =0;data1.length>k;k++) {	//요일 추출
					day1[k] = ""+data1[k].charAt(0);
				}
				
				LocalDateTime time1 [][] = new LocalDateTime[2][2];
				LocalDateTime time2 [][] = new LocalDateTime[2][2];
				boolean overlap = false;	//시간 중복여부
				boolean isDiscovery = false;
				System.out.println(data1[0]+"ajsfdlkjdsafkl;sdlkalasfasd");
				
				for(int idx=0;data1.length>idx;idx++) {
					System.out.println(data1[idx]);
						
					
					str= data1[idx].substring(1);
					String divTime[] = str.split("-");//9:00:-10:00 분리
					String addDay="20210101 ";	//시간을 비교하기위한 가짜 날짜
					for(int k =0;divTime.length>k;k++) {	//나눠진 시간을 시간타입으로 변경
						time1 [idx][k] = LocalDateTime.parse(addDay+divTime[k],DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
					}
				}
					
				for(int j=i+1; j<scheduleList.size();j++) {System.out.println("---------"+scheduleList.get(i).getLecture()+" <> "+scheduleList.get(j).getLecture());
					str = scheduleList.get(j).getLectureTime();
					System.out.println("First Check"+str);
					if(str.equals("미지정") || str.equals("본교가상") || str.contains("MOOC")) {//강의시간이 없을경우
						continue;
					}
					String data2 [] = scheduleList.get(j).getLectureTime().replace("(", "").replace(")", "").split("\\|");
					day2= new String[data2.length];
					for(int k =0;data2.length>k;k++) {	//요일 추출
						day2[k] = ""+data2[k].charAt(0);
					}
					
					for(int idx=0;data2.length>idx;idx++) {
						System.out.println(data2[idx]);	
						System.out.println("||"+str);
						str= data2[idx].substring(1);
						String divTime[] = str.split("-");//9:00:-10:00 분리
						String addDay="20210101 ";	//시간을 비교하기위한 가짜 날짜
						for(int k =0;divTime.length>k;k++) {	//나눠진 시간을 시간타입으로 변경
							time2 [idx][k] = LocalDateTime.parse(addDay+divTime[k],DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
						}
					}
					
					/*********************중복검사!************************************/
					for(int idx=0;day1.length>idx;idx++) {
						for(int k=0;day2.length>k;k++) {
							System.out.println("인덱스: "+idx+" 요일: "+day1[idx]+"compare"+day2[k]);
							if(day1[idx].equals(day2[k])) {	//요일이 같을경우
								System.out.print("true");
								if(time1[idx][1].isAfter(time2[k][0])) {//끝1 <-> 시2 여긴다시....손대기...
									System.out.println(time1[idx][1]+"compare"+time2[k][0]);
									//if(time1[idx][1].isAfter(time2[k][1])) {//끝1 <-> 끝2
									if(time1[idx][0].isAfter(time2[k][1])) {//시1 <-> 끝2
										//중복아님
										overlap = false;System.out.println(time1[idx][1]+"compare"+time2[k][1]);
										break;//revise
									}else {
										System.out.println(time1[idx][1]+"compare"+time2[k][1]);
										//중복
										overlap = true;System.out.println("중복1");
										break;
									}
								}else {
									//가상또는 시간 없음
									overlap = false;
									break;//revise
								}
							}else {//요일이 다를경우
								overlap = false;
								break;//revise
							}
							
						}
						if(overlap) {
							break;
						}
						
					}
					
					
					if(overlap) {//그냥 위랑 합쳐도될듯
						if(!isDiscovery) {//if(false) {
							array.add(printList(scheduleList.get(i),scheduleList.get(i).getLectureNumber()));
							isDiscovery=true;
						}
						array.add(printList(scheduleList.get(j),scheduleList.get(i).getLectureNumber()));
					}
					
				}
				
			}
			
		}
		
		data.put("list",array);
		
		String jsonInfo = data.toJSONString();
		if(array.size()==0) {	//데이터가 없을경우
			jsonInfo="noData";
		}
		System.out.println(jsonInfo);
		out.print(jsonInfo);
			
	}
}
