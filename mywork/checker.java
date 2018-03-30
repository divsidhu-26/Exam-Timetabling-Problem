import java.io.*;
import java.util.*;
class Period{		// assuming length of every period is same
	int id_no;
	int capacity = 0;
	int session;	// sessions are morning == 1 afternoon == 2 evening == 3
	Room[] room = new Room[100];
	List<Exam> exams = new ArrayList<Exam>();
	public Period(int id,int session,int rooms){
		this.id_no = id;
		this.session = session;
		for(int i = 0;i<rooms;i++){
			room[i] = new Room(i);
		}
	}
}

class Room{
	int capacity;
	int id_no;
	List<Exam> exams = new ArrayList<Exam>();
	public Room(int id){
		this.id_no = id;
	}
	public Boolean contains_exam(Exam a){
		for(Iterator<Exam> iter = this.exams.iterator(); iter.hasNext();) {
		    Exam data = iter.next();
		    if (data.identifier == a.identifier) {
		        return true;
		    }
		}
		return false;
	}
}

class Exam{
	List<Student> students = new ArrayList<Student>();
	int no_of_students=0;
	String identifier;

	public Exam(String a){
		this.identifier = a;
	}

}

class Student{
	String id;
	List<Exam> exams = new ArrayList<Exam>();
	List<Integer> periods = new ArrayList<Integer>();
	public Student(String a){
		this.id = a;
	}


}


class checker{
	public static Boolean contains_student(List<Student> a,String s){
		int len = a.size();
		for(int i=0;i<len;i++){
			if((a.get(i).id).equals(s)){
				return true;
			}
		}
		return false;
	}

	public static int find_exam(List<Exam> a, String s){
		Iterator<Exam> iter_a = a.iterator();int count=0;
		while(iter_a.hasNext()){
			Exam b = iter_a.next();
			if(b.identifier.equals(s)){
				return count;
			}
			else{count++;}
		}
		return -1;
	}

	public static int find_student(List<Student> a, String s){
		Iterator<Student> iter_a = a.iterator();int count=0;
		while(iter_a.hasNext()){
			Student b = iter_a.next();
			if(b.id.equals(s)){
				return count;
			}
			else{count++;}
		}
		return -1;
	}
	public static void main(String[] args){
		int num_rooms = 1;
		int num_periods = Integer.parseInt(args[0]);
		int room_size = Integer.parseInt(args[1]);
		List<Exam> exams = new ArrayList<Exam>();
		List<Exam> unscheduled_exams = new ArrayList<Exam>();
		List<Student> students = new ArrayList<Student>();
		try{
			File file = new File(args[2]);
			Scanner scan_exams = new Scanner(file);
			String dummy_exams;
	//		System.out.println(scan_exams.nextLine());
			while(scan_exams.hasNextLine()){
				dummy_exams = scan_exams.nextLine();
				StringTokenizer st = new StringTokenizer(dummy_exams);
				Exam dummy_exam = new Exam(st.nextToken());
				exams.add(dummy_exam);
	//			dummy_exams = scan_exams.nextLine();

			}

			File file_students = new File(args[3]);
			Scanner scan_students = new Scanner(file_students);
			String dummy_students = "";
			while(scan_students.hasNextLine()){
				dummy_students = scan_students.nextLine();
				StringTokenizer st2 = new StringTokenizer(dummy_students);
				String name = st2.nextToken();
				String subject = st2.nextToken();
				if(contains_student(students,name)){
					int stc = find_student(students,name);
					int exc =  find_exam(exams,subject);
					Exam thisone = exams.get(exc);
					Student dummy_student = students.get(stc);
					dummy_student.exams.add(thisone);
					students.set(stc,dummy_student);
					thisone.students.add(dummy_student);
					exams.set(exc,thisone);
		//			System.out.println(students.get(stc).exams.size());
				}
				else{
					Student dummy_student = new Student(name);
					int exc =  find_exam(exams,subject);
					Exam thisone = exams.get(exc);
					dummy_student.exams.add(thisone);
					students.add(dummy_student);
					thisone.students.add(dummy_student);
					exams.set(exc,thisone);
				}
			}
		}catch(Exception e){e.printStackTrace();}
		int x = 0,y=0;
		String file_name = args[4];
		List<Period> period_list = new ArrayList<Period>();
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("check_"+args[2]));		
			File file = new File(file_name);
			Scanner scan = new Scanner(file);
			while(scan.hasNext()){
				int mo = -1;
				for(int m=0;m<num_periods;m++){
					mo++;
					Period this_per = new Period(m,m%3,num_rooms);
					String period_string = scan.nextLine();
				//	System.out.println(period_string);
					for(int k=0;k<num_rooms;k++){
						Room this_rm = this_per.room[k];
						if(!scan.hasNext()) continue;
						String exams_in_room = scan.nextLine();
						System.out.println(exams_in_room+" "+m);
				//		bw.write(exams_in_room+"        "+m+"\n");
						StringTokenizer st = new StringTokenizer(exams_in_room,";:");
						String dummy = st.nextToken();
				//		System.out.println(exams_in_room+" sad "+dummy+" asd "+st.countTokens());
						int j=0;
						while(st.hasMoreTokens()){
							System.out.println(m+" "+k+" "+j);
							j++;
							String this_exam_id = st.nextToken();
							int exam_index = find_exam(exams,this_exam_id);
							if(exam_index<0)	continue;
							Exam this_exam = exams.get(exam_index);
							x++;
							Iterator<Student> it = this_exam.students.iterator();
							while(it.hasNext()){
								Student stu = it.next();
								stu.periods.add(m);
								y++;
							}
						}
					}
//					System.out.println(x);	
				}
			}
	//		System.out.println(x+" "+y);
			x = 0;y=0;
			Iterator<Student> it_stu = students.iterator();
			while(it_stu.hasNext()){
				Student a = it_stu.next();
				Iterator<Integer> num = (a.periods).iterator();
				int lastp = -10;
	//			bw.write(a.exams.size()+"\n");
				while(num.hasNext()){
					int p = num.next();
//					System.out.print(p+" ");
					bw.write(p+" ");
					if(lastp == p){
						y++;
						bw.write("sd "+p+" "+lastp+" "+a.id);
					}
					if((p-lastp)==1 && ((p%16)%3-(lastp%16)%3)==1){
						x++;
//						System.out.println("saa"+p+" "+lastp+" "+a.id);
						bw.write("saa"+p+" "+lastp+" "+a.id);
					}
					lastp = p;

				}
				bw.write("\n");
//				System.out.println();
			}	
			System.out.println(x+" "+y);
			bw.close();
		}catch(Exception e){e.printStackTrace();}		
	}
}