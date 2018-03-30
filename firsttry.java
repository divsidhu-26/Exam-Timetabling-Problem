import java.io.*;
import java.util.*;

class Period{		// assuming length of every period is same
	int id_no;
	int session;	// sessions are morning == 1 afternoon == 2 evening == 3
	Room room = new Room(1,1600,36);
	List<Exam> exams = new ArrayList<Exam>();
	public Period(int id,int session){
		this.id_no = id;
		this.session = session;
	}
}
class Room{
	int capacity;
	int id_no;
	int size;
	public Room(int id,int capacity,int size){
		this.id_no = id;
		this.capacity = capacity;
		this.size = size;
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

	public Student(String a){
		this.id = a;
	}


}
class firsttry{
	public static Boolean compare_students(Exam a, Exam b){
		Iterator<Student> iter_a = a.students.iterator();
		while(iter_a.hasNext()){
			if(contains_student(b.students,iter_a.next().id))
				return true;
		}
		return false;
	}

	public static int compare_students_num(Exam a, Exam b){
		int res = 0;
		Iterator<Student> iter_a = a.students.iterator();
		while(iter_a.hasNext()){
			if(contains_student(b.students,iter_a.next().id))
				res++;
		}
		return res;
	}

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
		return 0;
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
		return 0;
	}

	public static int penalty(Period[] period,int i,Exam tobeputintable){
		int res = 0;
		List<Exam> dummy_exam = new ArrayList(period[i].exams);
		Iterator<Exam> iter_period = (dummy_exam).iterator();
		while(iter_period.hasNext()){
			Exam dummy = iter_period.next();
			if(compare_students(dummy,tobeputintable)){
				res += compare_students_num(dummy,tobeputintable);
			}
		}
		if(period[i].session == 1 && i<32){
			List<Exam> nextperiod = new ArrayList(period[i+1].exams);
			Iterator<Exam> iter_nextperiod = nextperiod.iterator();
			while(iter_nextperiod.hasNext()){
				Exam dummynext = iter_nextperiod.next();
				if(compare_students(dummynext,tobeputintable)){
					res += compare_students_num(dummynext,tobeputintable);
					res++;
				}
			}//System.out.print(res+" ");
		}
		if(period[i].session == 3 && i>0){
			List<Exam> lastperiod = new ArrayList(period[i-1].exams);
			Iterator<Exam> iter_lastperiod = lastperiod.iterator();
			while(iter_lastperiod.hasNext()){
				Exam dummylast = iter_lastperiod.next();
				if(compare_students(dummylast,tobeputintable)){
					res += compare_students_num(dummylast,tobeputintable);
					res++;
				}
			}//System.out.print(res+" ");
		}
		if(period[i].session == 2 && i>0 && i<32){
			List<Exam> nextperiod = new ArrayList(period[i+1].exams);
			Iterator<Exam> iter_nextperiod = nextperiod.iterator();
			while(iter_nextperiod.hasNext()){
				Exam dummynext = iter_nextperiod.next();
				if(compare_students(dummynext,tobeputintable)){
					res += compare_students_num(dummynext,tobeputintable);
					res++;
				}
			}
			List<Exam> lastperiod = new ArrayList(period[i-1].exams);
			Iterator<Exam> iter_lastperiod = lastperiod.iterator();
			while(iter_lastperiod.hasNext()){
				Exam dummylast = iter_lastperiod.next();
				if(compare_students(dummylast,tobeputintable)){
					res += compare_students_num(dummylast,tobeputintable);
					res++;
				}
			}//System.out.print(res+" ");
		}

		return res;
	}

	public static void main(String[] args){
		List<Exam> exams = new ArrayList<Exam>();
		List<Exam> unscheduled_exams = new ArrayList<Exam>();
		List<Student> students = new ArrayList<Student>();
		try{
			File file = new File("exams");
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

			File file_students = new File("enrolements");
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

		Period[] period = new Period[100];
		for(int i=1;i<101;i++){
			int session = i % 3;
			if(session == 0)	session = 3;
			period[i-1] = new Period(i,session);
		}


		Iterator<Exam> iter = exams.iterator();
		while(iter.hasNext()){
			Exam tobeputintable = iter.next();
			int flag1=0;
			while(flag1 == 0){
				int flag2 = 0,flag3=0,flag4=0;
				for(int i=0;i<100 && flag1==0;i++){
					flag3=0;flag2=0;flag4=0;
					int capacity = period[i].room.capacity;
			//		if(period[i].room.size<1) continue;
			//		period[i].room.size--;
					List<Exam> dummy_exam = new ArrayList(period[i].exams);
					Iterator<Exam> iter_period = (dummy_exam).iterator();
					if(period[i].session == 1 && i<99){
						List<Exam> nextperiod = new ArrayList(period[i+1].exams);
						Iterator<Exam> iter_nextperiod = nextperiod.iterator();
						while(iter_nextperiod.hasNext() && flag4==0){
							Exam dummynext = iter_nextperiod.next();
							if(compare_students(dummynext,tobeputintable)){
								flag4 = 1;
							}
						}
					}
					if(period[i].session == 3 && i>0){
						List<Exam> lastperiod = new ArrayList(period[i-1].exams);
						Iterator<Exam> iter_lastperiod = lastperiod.iterator();
						while(iter_lastperiod.hasNext() && flag4==0){
							Exam dummylast = iter_lastperiod.next();
							if(compare_students(dummylast,tobeputintable)){
								flag4 = 1;
							}
						}
					}
					if(period[i].session == 2 && i>0 && i<99){
						List<Exam> nextperiod = new ArrayList(period[i+1].exams);
						Iterator<Exam> iter_nextperiod = nextperiod.iterator();
						while(iter_nextperiod.hasNext() && flag4==0){
							Exam dummynext = iter_nextperiod.next();
							if(compare_students(dummynext,tobeputintable)){
								flag4 = 1;
							}
						}
						List<Exam> lastperiod = new ArrayList(period[i-1].exams);
						Iterator<Exam> iter_lastperiod = lastperiod.iterator();
						while(iter_lastperiod.hasNext() && flag4==0){
							Exam dummylast = iter_lastperiod.next();
							if(compare_students(dummylast,tobeputintable)){
								flag4 = 1;
							}
						}
					}//System.out.println(i+" "+penalty(period,i,tobeputintable)+" "+flag4);
						
					if(flag4 == 1){
						continue;
					}
					if(!iter_period.hasNext()){
						if(capacity > tobeputintable.students.size()){
							period[i].exams.add(tobeputintable);
							flag1=1;
							period[i].room.capacity = capacity - tobeputintable.students.size();
							flag3=1;
							System.out.println("this is first "+ tobeputintable.identifier + " of "+ i+" capacity = "+period[i].room.capacity);
						}
					}
					while(iter_period.hasNext() && flag2==0){
						Exam dummy = iter_period.next();
						if(compare_students(dummy,tobeputintable)){
					//		System.out.println(dummy.identifier+" "+tobeputintable.identifier);
							flag3=1;
							flag2=1;
						}
					}
				//	System.out.println("yo");
				//	if(flag1==0)  System.out.println("flag3 is "+ flag3);
					if(flag3 == 0){
						if(capacity > tobeputintable.students.size()){
							period[i].exams.add(tobeputintable);
							period[i].room.capacity = capacity - tobeputintable.students.size();
							System.out.println("The "+period[i].exams.size()+" of "+i+" is "+ tobeputintable.identifier + " .free seats = "+period[i].room.capacity);
							flag1=1;
						}
					}
				}

				if(flag1 == 0){
					flag1=1;
					System.out.println("not in table" + tobeputintable.identifier);
				}
			}
		}
		int conflicts = 0;
		for(int i = 32; i<100;i++){
			Period dummy = period[i];
			List<Exam> dummy_exam = dummy.exams;
			Iterator<Exam> iter_exam = dummy_exam.iterator();
			while(iter_exam.hasNext()){
				Exam tobeputintable = iter_exam.next();
				int minpenalty = 1000000,min_index = -1;
				int cap = tobeputintable.no_of_students;
				for(int j = 1;j<32;j++){
			//	System.out.println(period[i].room.size+" "+j);
					if(period[i].room.size<1)	continue;
					int room_cap = period[j].room.capacity;
					if(cap > room_cap)	continue;
					int pen = penalty(period,j,tobeputintable);
			//		System.out.println(pen+ " ");
					if(pen<minpenalty){
						minpenalty = pen;
						min_index = j;
					}
					if(minpenalty == 0)	break;
				}
				if(min_index == -1){	
					unscheduled_exams.add(tobeputintable);
					System.out.println(tobeputintable.identifier+" is unscheduled");
				}
				else{
					period[min_index].room.size--;
					conflicts += minpenalty;
					period[min_index].exams.add(tobeputintable);
					period[min_index].room.capacity = period[min_index].room.capacity - cap;
					System.out.println(tobeputintable.identifier+" is added to period "+min_index+" penalty is "+minpenalty);
				}
			}	
		}
		System.out.println(conflicts);


	}
}