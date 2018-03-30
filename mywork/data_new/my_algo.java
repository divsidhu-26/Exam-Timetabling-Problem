import java.io.*;
import java.util.*;

class Period{		// assuming length of every period is same
	int id_no;
	int session;	// sessions are morning == 0 afternoon == 1 evening == 2
	Room[] room = new Room[1];
	List<Exam> exams = new ArrayList<Exam>();
	public Period(int id,int session,int size){
		this.id_no = id;
		this.session = session;
		room[0] = new Room(0,size);
	}
	public Boolean isAvailable(int cap){
		for(int i=0;i<1;i++){
			if(room[i].capacity > cap)
				return true;
		}
		return false;
	}

	public void allot(Exam a){
		int cap = a.students.size();
		for(int i = 0;i >=0;i--){
			if(cap < room[i].capacity){
				room[i].exams.add(a);
				room[i].capacity -= cap;
				this.exams.add(a);
				break;
			}
		}
	}

	public void delete(Exam a){
		int cap = a.students.size();
		for(int i = 0;i < 1;i++){
			if(room[i].contains_exam(a)){
				for(Iterator<Exam> iter = room[i].exams.iterator(); iter.hasNext();) {
				    Exam data = iter.next();
				    if (data.identifier == a.identifier) {
				        iter.remove();
				        room[i].capacity += cap;
				    }
				}		
			}
		}
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

	public Student(String a){
		this.id = a;
	}


}

class my_algo{
	public static Boolean compare_students(Exam a, Exam b){					//true if students are common between the exams
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

	public static Boolean penalty(Period[] period,int i,Exam tobeputintable){
		int res = 0;
		List<Exam> thisperiod = new ArrayList(period[i].exams);
		Iterator<Exam> iter_thisperiod = thisperiod.iterator();
		
		while(iter_thisperiod.hasNext()){
			Exam thisone = iter_thisperiod.next();
			if(thisone.identifier.equals(tobeputintable.identifier)){
				continue;
			}
			if(compare_students(thisone,tobeputintable)){
				return false;
			}
		}
		if(period[i].session == 0 && i<99 && period[i+1].session == 1){
			List<Exam> nextperiod = new ArrayList(period[i+1].exams);
			Iterator<Exam> iter_nextperiod = nextperiod.iterator();
			while(iter_nextperiod.hasNext()){
				Exam dummynext = iter_nextperiod.next();
				if(dummynext.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummynext,tobeputintable)){
					return false;
				}
			}//System.out.print(res+" ");
		}
		else if(period[i].session == 2){
			List<Exam> lastperiod = new ArrayList(period[i-1].exams);
			Iterator<Exam> iter_lastperiod = lastperiod.iterator();
			while(iter_lastperiod.hasNext()){
				Exam dummylast = iter_lastperiod.next();
				if(dummylast.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummylast,tobeputintable)){
					return false;
				}
			}//System.out.print(res+" ");
		}
		else if(period[i].session == 1){
			List<Exam> nextperiod = new ArrayList(period[i+1].exams);
			Iterator<Exam> iter_nextperiod = nextperiod.iterator();
			while(iter_nextperiod.hasNext()){
				Exam dummynext = iter_nextperiod.next();
				if(dummynext.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummynext,tobeputintable)){
					return false;
				}
			}
		//	System.out.println(i);

			List<Exam> lastperiod = new ArrayList(period[i-1].exams);
			Iterator<Exam> iter_lastperiod = lastperiod.iterator();
			while(iter_lastperiod.hasNext()){
				Exam dummylast = iter_lastperiod.next();
				if(dummylast.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummylast,tobeputintable)){
					return false;	
				}
			}//System.out.print(res+" ");
		}
	
		return true;
	}

	public static int penalty_count(Period[] period,int i,Exam tobeputintable){
		int res = 0;
		List<Exam> dummy_exam = new ArrayList(period[i].exams);
		Iterator<Exam> iter_period = (dummy_exam).iterator();
		while(iter_period.hasNext()){
			Exam thisone = iter_period.next();
			if(thisone.identifier.equals(tobeputintable.identifier)){
				continue;
			}
			if(compare_students(thisone,tobeputintable)){
				res += 100000000;
			}
		}
		if(period[i].session == 0 && i<99 && period[i+1].session == 1){
			List<Exam> nextperiod = new ArrayList(period[i+1].exams);
			Iterator<Exam> iter_nextperiod = nextperiod.iterator();
			while(iter_nextperiod.hasNext()){
				Exam dummynext = iter_nextperiod.next();
				if(dummynext.identifier.equals(tobeputintable.identifier)){
					continue;
				}	
				if(compare_students(dummynext,tobeputintable)){
					res += compare_students_num(dummynext,tobeputintable);
				}
			}//System.out.print(res+" ");
		}
		if(period[i].session == 2){
			List<Exam> lastperiod = new ArrayList(period[i-1].exams);
			Iterator<Exam> iter_lastperiod = lastperiod.iterator();
			while(iter_lastperiod.hasNext()){
				Exam dummylast = iter_lastperiod.next();
				if(dummylast.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummylast,tobeputintable)){
					res += compare_students_num(dummylast,tobeputintable);
				}
			}//System.out.print(res+" ");
		}
		if(period[i].session == 1){
			List<Exam> nextperiod = new ArrayList(period[i+1].exams);
			Iterator<Exam> iter_nextperiod = nextperiod.iterator();
			while(iter_nextperiod.hasNext()){
				Exam dummynext = iter_nextperiod.next();
				if(dummynext.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummynext,tobeputintable)){
					res += compare_students_num(dummynext,tobeputintable);
				}
			}
			List<Exam> lastperiod = new ArrayList(period[i-1].exams);
			Iterator<Exam> iter_lastperiod = lastperiod.iterator();
			while(iter_lastperiod.hasNext()){
				Exam dummylast = iter_lastperiod.next();
				if(dummylast.identifier.equals(tobeputintable.identifier)){
					continue;
				}
				if(compare_students(dummylast,tobeputintable)){
					res += compare_students_num(dummylast,tobeputintable);
				}
			}//System.out.print(res+" ");
		}

		return res;
	}

	public static int find_total_conflicts(Period[] period,int start,int end){
		int res = 0;
		for(int i=start;i<end;i++){
			List<Exam> dummy_exam = new ArrayList(period[i].exams);
			Iterator<Exam> iter_period = (dummy_exam).iterator();
			while(iter_period.hasNext()){
				Exam test = iter_period.next();
				if(period[i].session < 2 && period[i+1].session > 0){
					List<Exam> exams = period[i+1].exams;
					Iterator<Exam> it = exams.iterator();
					while(it.hasNext()){
						Exam a = it.next();
						res += compare_students_num(a,test);
					}
				}
				if(period[i].session > 0){
					List<Exam> exams = period[i-1].exams;
					Iterator<Exam> it = exams.iterator();
					while(it.hasNext()){
						Exam a = it.next();
						res += compare_students_num(a,test);
					}
				}
			}	
	//	System.out.println(res+" "+i);
		}

		return res;
	}

	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
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
				dummy_exam.no_of_students = Integer.parseInt(st.nextToken());
				exams.add(dummy_exam);
	//			dummy_exams = scan_exams.nextLine();
			}

			File file_students = new File(args[3]);
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
		Period[] period = new Period[100];
		for(int i=0;i<100;i++){
			int session = (i%16) % 3;
			period[i] = new Period(i,session,room_size);
		}

		Collections.shuffle(exams);

		int maxi = 0,pen = 0;
		Iterator<Exam> iter_exam = exams.iterator();
		while(iter_exam.hasNext()){
			Exam tobeputintable = iter_exam.next();
			for(int i = 0;i<100;i++){
				Period current = period[i];
				if(penalty(period,i,tobeputintable)){
					if(current.isAvailable(tobeputintable.students.size())){	
						
						pen += penalty_count(period,i,tobeputintable);
						period[i].allot(tobeputintable);
				//		System.out.println(tobeputintable.identifier + " " + i);
						if(i > maxi){	maxi = i;}
						break;
					}	
				}
				if(i == 99){
					unscheduled_exams.add(tobeputintable);
				}
			}

		}			
		int c = 0;
		
		System.out.println(maxi + " periods have been filled in the initialization.");
		c = 0;
		for(int i = 0;i<=maxi;i++){
			System.out.println("Period "+i+":");
			for(int j = 0;j<1;j++){
				System.out.print("Room No. "+j+" : ");
		//		System.out.println("YES");
				iter_exam = period[i].room[j].exams.iterator();
				while(iter_exam.hasNext()){
					Exam d = iter_exam.next();
					System.out.print(d.identifier+"; ");
					c++;
				}System.out.println();
			}
			System.out.println(c);
		}

		int conflicts = 0,prev_conflicts = 0;
		for(int i = num_periods;i <= maxi;i++){
			Period dummy = period[i];
			List<Exam> dummy_exam = dummy.exams;
		//	System.out.println(dummy_exam);
			iter_exam = dummy_exam.iterator();
			while(iter_exam.hasNext()){
				Exam tobeputintable = iter_exam.next();
				int minpenalty = 1000000,min_index = -1;
				int cap = tobeputintable.students.size();
				for(int j = 0;j<num_periods;j++){
					if(!period[j].isAvailable(tobeputintable.students.size()))	
						continue;
					pen = penalty_count(period,j,tobeputintable);
					if(pen<minpenalty){
						minpenalty = pen;
						min_index = j;
					}
					if(minpenalty == 0)	break;
				}
				if(min_index == -1){	
					unscheduled_exams.add(tobeputintable);
		//			System.out.println(tobeputintable.identifier+" is unscheduled");
				}
				else{
					conflicts += minpenalty;
					period[min_index].allot(tobeputintable);
					iter_exam.remove();
		//			System.out.println(tobeputintable.identifier+" is added to period "+min_index+" penalty is "+minpenalty);
				}
			}
		}
		c=0;
	
		prev_conflicts = find_total_conflicts(period,0,num_periods)/2;
		System.out.println(conflicts+" "+prev_conflicts+" "+unscheduled_exams.size());
		prev_conflicts = conflicts;
		





		do{
			prev_conflicts = conflicts;
			conflicts = 0;
			for(int i = 0;i<num_periods;i++){
				Period current = period[i];
				List<Exam> dummy_exam = current.exams;
				iter_exam = dummy_exam.iterator();
				while(iter_exam.hasNext()){
					Exam tobeputintable = iter_exam.next();
					int cap = tobeputintable.students.size();
					int currentpen = penalty_count(period,i,tobeputintable);
					int minpenalty = currentpen,min_index = i;
					if(currentpen == 0){	
						System.out.println(i+" "+tobeputintable.identifier);
						continue;
					}	
					for(int j = 0;j<num_periods;j++){
						if(j == i)	continue;
						if(!period[j].isAvailable(tobeputintable.students.size()))	
							continue;
						pen = penalty_count(period,j,tobeputintable);
						if(pen<minpenalty){
							minpenalty = pen;
							min_index = j;
						}
						if(minpenalty == 0)	break;
					}
					if(minpenalty > currentpen)	continue;
					if(min_index == i){	
						System.out.println(tobeputintable.identifier+" stays where it is. penalty is "+ currentpen);
					}
					else{
						period[min_index].allot(tobeputintable);
						iter_exam.remove();		
						period[i].delete(tobeputintable);																	//remove from rooms
						System.out.println(tobeputintable.identifier+" is added to period "+min_index+" penalty is "+minpenalty);
					}
				}

				System.out.println("THERE "+i);
			}
			iter_exam = unscheduled_exams.iterator();
			while(iter_exam.hasNext()){
				Exam tobeputintable = iter_exam.next();
				int minpenalty = 1000000,min_index = -1;
				int cap = tobeputintable.students.size();
				for(int j = 0;j<num_periods;j++){
					if(!period[j].isAvailable(tobeputintable.students.size()))	
						continue;
					pen = penalty_count(period,j,tobeputintable);
					if(pen<minpenalty){
						minpenalty = pen;
						min_index = j;
					}
					if(minpenalty == 0)	break;
				}
				if(min_index == -1){
					System.out.println(minpenalty+" ");
				}
				else{
					period[min_index].allot(tobeputintable);
					iter_exam.remove();		
					System.out.println(tobeputintable.identifier+" is added to period "+min_index+" penalty is "+minpenalty);
				}
			}	
			System.out.println("HERE");
			conflicts = find_total_conflicts(period,0,num_periods)/2;
			System.out.println(conflicts+" "+prev_conflicts);

			
		}while(prev_conflicts != conflicts);
		System.out.println(conflicts+" "+find_total_conflicts(period,0,num_periods));

		c = 0;
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(args[4]));		
			for(int i = 0;i<num_periods;i++){
				System.out.println("Period "+(i)+":");
				bw.write("Period "+(i)+":\n");
				for(int j = 0;j<1;j++){
					System.out.print("Room No. "+j+" : ");
					bw.write("Room No. "+j+" :");
			//		System.out.println("YES");
					iter_exam = period[i].room[j].exams.iterator();
					while(iter_exam.hasNext()){
						Exam d = iter_exam.next();
						System.out.print(d.identifier+"; ");
						bw.write(d.identifier+";");
						c++;
					}System.out.println();
					bw.write("\n");
				}
				System.out.println(c);

			}
			bw.close();
		}catch(Exception e){e.printStackTrace();}

		System.out.println("unscheduled_exams : "+unscheduled_exams.size()+" "+conflicts);
		iter_exam = unscheduled_exams.iterator();
		while(iter_exam.hasNext()){
			Exam dummy_exam = iter_exam.next();
			System.out.println(dummy_exam.identifier);
			System.out.println(dummy_exam.students.size());
			for(int i=0;i<num_periods;i++){
				if(period[i].isAvailable(dummy_exam.students.size())){
					System.out.println(penalty_count(period,i,dummy_exam)/2);
				}
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
		System.out.println(unscheduled_exams.size()+" "+find_total_conflicts(period,0,num_periods)/2);
	}	
}	