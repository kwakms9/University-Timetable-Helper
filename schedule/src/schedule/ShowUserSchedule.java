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

	protected JSONObject printList(ScheduleBean schedule, String number) {	//�ð� �ߺ��� ��
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
			info.put("overlapNumber", number);	//�ð� �ߺ�����
			return info;
	}
	
	protected JSONObject printList(ScheduleBean schedule) {	//���
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
		
		userScheduleList =dao.getScheduleList(userId);	//����ڰ� ������ ������ ��������
		
		String query ="";
		for(UserScheduleBean list : userScheduleList) {	//������ ������� ���� ������ ������ �����
			query+="������ȣ="+list.getLectureNumber()+" or ";
		}
		if(userScheduleList.size()>0) {
			query = query.substring(0, query.length()-3); //�ǵ��� or �����
		}else {
			out.print("noData");
			return;
		}
		System.out.println("query = "+query);
		
		
		List <ScheduleBean> scheduleList;
		scheduleList =sdao.getSchuduleInfo(query);	//������ȣ ����
		
		if(loadType.equals("selected")) {	//������ ����� ��� ���
			for(ScheduleBean schedule : scheduleList) {
				array.add(printList(schedule));
			}
			
		}else {
			//�ߺ����� ���� �� ���
			for(int i =0; i<scheduleList.size()-1;i++){
				String str = scheduleList.get(i).getLectureTime();
				if(str.equals("������") || str.equals("��������") || str.contains("MOOC")) {//���ǽð��� �������
					continue;
				}
				str = scheduleList.get(i).getLectureTime().replace("(", "").replace(")", "");//��ȣ �����
				String data1 [] = str.split("\\|");
				String day1 [] = new String[data1.length];/*******���߿� ������ �����ϱ�,,,,���ʿ� ������ �� �и� ����Ʈ�����ؼ� ���ϱ��??**********/
				String day2 [];
				
				for(int k =0;data1.length>k;k++) {	//���� ����
					day1[k] = ""+data1[k].charAt(0);
				}
				
				LocalDateTime time1 [][] = new LocalDateTime[2][2];
				LocalDateTime time2 [][] = new LocalDateTime[2][2];
				boolean overlap = false;	//�ð� �ߺ�����
				boolean isDiscovery = false;
				System.out.println(data1[0]+"ajsfdlkjdsafkl;sdlkalasfasd");
				
				for(int idx=0;data1.length>idx;idx++) {
					System.out.println(data1[idx]);
						
					
					str= data1[idx].substring(1);
					String divTime[] = str.split("-");//9:00:-10:00 �и�
					String addDay="20210101 ";	//�ð��� ���ϱ����� ��¥ ��¥
					for(int k =0;divTime.length>k;k++) {	//������ �ð��� �ð�Ÿ������ ����
						time1 [idx][k] = LocalDateTime.parse(addDay+divTime[k],DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
					}
				}
					
				for(int j=i+1; j<scheduleList.size();j++) {System.out.println("---------"+scheduleList.get(i).getLecture()+" <> "+scheduleList.get(j).getLecture());
					str = scheduleList.get(j).getLectureTime();
					System.out.println("First Check"+str);
					if(str.equals("������") || str.equals("��������") || str.contains("MOOC")) {//���ǽð��� �������
						continue;
					}
					String data2 [] = scheduleList.get(j).getLectureTime().replace("(", "").replace(")", "").split("\\|");
					day2= new String[data2.length];
					for(int k =0;data2.length>k;k++) {	//���� ����
						day2[k] = ""+data2[k].charAt(0);
					}
					
					for(int idx=0;data2.length>idx;idx++) {
						System.out.println(data2[idx]);	
						System.out.println("||"+str);
						str= data2[idx].substring(1);
						String divTime[] = str.split("-");//9:00:-10:00 �и�
						String addDay="20210101 ";	//�ð��� ���ϱ����� ��¥ ��¥
						for(int k =0;divTime.length>k;k++) {	//������ �ð��� �ð�Ÿ������ ����
							time2 [idx][k] = LocalDateTime.parse(addDay+divTime[k],DateTimeFormatter.ofPattern("yyyyMMdd HH:mm"));
						}
					}
					
					/*********************�ߺ��˻�!************************************/
					for(int idx=0;day1.length>idx;idx++) {
						for(int k=0;day2.length>k;k++) {
							System.out.println("�ε���: "+idx+" ����: "+day1[idx]+"compare"+day2[k]);
							if(day1[idx].equals(day2[k])) {	//������ �������
								System.out.print("true");
								if(time1[idx][1].isAfter(time2[k][0])) {//��1 <-> ��2 ����ٽ�....�մ��...
									System.out.println(time1[idx][1]+"compare"+time2[k][0]);
									//if(time1[idx][1].isAfter(time2[k][1])) {//��1 <-> ��2
									if(time1[idx][0].isAfter(time2[k][1])) {//��1 <-> ��2
										//�ߺ��ƴ�
										overlap = false;System.out.println(time1[idx][1]+"compare"+time2[k][1]);
										break;//revise
									}else {
										System.out.println(time1[idx][1]+"compare"+time2[k][1]);
										//�ߺ�
										overlap = true;System.out.println("�ߺ�1");
										break;
									}
								}else {
									//����Ǵ� �ð� ����
									overlap = false;
									break;//revise
								}
							}else {//������ �ٸ����
								overlap = false;
								break;//revise
							}
							
						}
						if(overlap) {
							break;
						}
						
					}
					
					
					if(overlap) {//�׳� ���� ���ĵ��ɵ�
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
		if(array.size()==0) {	//�����Ͱ� �������
			jsonInfo="noData";
		}
		System.out.println(jsonInfo);
		out.print(jsonInfo);
			
	}
}
