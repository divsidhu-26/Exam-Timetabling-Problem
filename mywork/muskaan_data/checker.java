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
		for(int i=0;i<rooms;i++){
			room[i] = new Room(0,100);
		}
		// room[1] = new Room(1,270);
		// room[2] = new Room(2,200);
		// room[3] = new Room(3,160);
		// room[4] = new Room(4,125);
		// room[5] = new Room(5,95);
		// room[6] = new Room(6,80);
		// room[7] = new Room(7,50);
		// room[8] = new Room(8,40);
		// room[9] = new Room(9,40);
		// room[10] = new Room(10,35);
		// room[11] = new Room(11,30);
		// room[12] = new Room(12,25);
		// room[13] = new Room(12,25);
		// room[14] = new Room(12,25);
		// room[15] = new Room(12,25);
		// room[16] = new Room(12,25);
		
	}
}

class Room{
	int capacity;
	int id_no;
	List<Exam> exams = new ArrayList<Exam>();
	public Room(int id,int capacity){
		this.id_no = id;
		this.capacity = capacity;
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
		int num_periods = Integer.parseInt(args[1]);
		List<Exam> exams = new ArrayList<Exam>();
		List<Exam> unscheduled_exams = new ArrayList<Exam>();
		List<Student> students = new ArrayList<Student>();
		try{
			File file = new File("tre.crs");
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

			File file_students = new File("tre");
			Scanner scan_students = new Scanner(file_students);
			String dummy_students = "";
			while(scan_students.hasNextLine()){
				dummy_students = scan_students.nextLine();
				StringTokenizer st2 = new StringTokenizer(dummy_students);
				if(st2.countTokens()==0)	continue;
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
		String file_name = args[0];
		List<Period> period_list = new ArrayList<Period>();
		try{
			File file = new File(file_name);
			Scanner scan = new Scanner(file);
			BufferedWriter bw = new BufferedWriter(new FileWriter(args[3]));		
			while(scan.hasNext()){
				for(int m=0;m<num_periods;m++){
					System.out.println("yo "+m+" "+num_periods);
					String period_string = scan.nextLine();
					bw.write(period_string+" "+m+"\n");
					Period this_per = new Period(0,0,0);
					if(m<16){
						this_per = new Period(m,m%3,Integer.parseInt(args[2]));
					}
					else if(m<32){
						this_per = new Period(m,(m-16)%3,Integer.parseInt(args[2]));
					}
					else{
						this_per = new Period(m,(m-32)%3,Integer.parseInt(args[2]));
					}
					for(int k=0;k<6;k++){
						Room this_rm = this_per.room[k];
						if(!scan.hasNext()) continue;
						String exams_in_room = scan.nextLine();
						StringTokenizer st = new StringTokenizer(exams_in_room,";:");
						if(st.countTokens() == 0)	continue;
				//		if(m == 30)				bw.write(exams_in_room+" "+st.countTokens()+"\n");
						String dummy = st.nextToken();
				//		if(m == 30)				bw.write(exams_in_room+" sad "+dummy+" asd "+st.countTokens()+"\n");
						int j=0;
						while(st.countTokens()>1){
				//			System.out.println(m+" "+k+" "+j);
							String this_exam_id = st.nextToken();
							if(m == 30)	System.out.println(this_exam_id);
							int exam_index = find_exam(exams,this_exam_id);
							if(exam_index > -1){ 	
								Exam this_exam = exams.get(exam_index);
								x++;
								Iterator<Student> it = this_exam.students.iterator();
								while(it.hasNext()){
									Student stu = it.next();
									stu.periods.add(m);
									y++;
								}
							}
							else{
								System.out.println(this_exam_id);
							}
						}
					}
			//		System.out.println(x);	
				}
			}
			System.out.println(x+" "+y);
			x = 0;y=0;
			Iterator<Student> it_stu = students.iterator();
			while(it_stu.hasNext()){
				Student a = it_stu.next();
				Iterator<Integer> num = (a.periods).iterator();
				int lastp = -10,lastps = -2;
				bw.write("Student "+a.id+": \n");
				while(num.hasNext()){
					int p = num.next(),ps=0;
					System.out.print(p+" ");
					bw.write(p+" ");
					if(p<16){
						ps = p%3;
					}
					else{
						if(p<32){
							ps = (p-16)%3;
						}
						else{
							ps = (p-32)%3;
						}
					}
					if((lastp==p)){
						x++;
						System.out.println(" saa"+p+" "+lastp+" "+a.id);
						bw.write(" saa "+p+" "+lastp+" "+a.id+"\n");
					}
					if((p-lastp)==1 && (ps-lastps)==1){
						y++;
						System.out.println(" sd"+p+" y:"+y+"  --"+lastp+" "+a.id);
						bw.write(" sd "+p+" y "+y+" "+lastp+" "+a.id+"\n");
					}
					lastp = p;
					lastps = ps;

				}
				bw.write("\n");
				System.out.println();
			}
			System.out.println(x+" "+y);
			bw.write(x+"\n"+y+"\n"+"dasddasdsa");
			bw.close();

		}catch(Exception e){e.printStackTrace();}		
	}
}