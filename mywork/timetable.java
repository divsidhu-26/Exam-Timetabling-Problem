import java.io.*;
import java.util.*;
class Period{		// assuming length of every period is same
	int id_no;
	int rooms;
	int session;	// sessions are morning == 0 afternoon == 1 evening == 2
	Room room;
	List<Exam> exams = new ArrayList<Exam>();
	public Period(int id,int session,int size){
		this.id_no = id;
		this.session = session;
		this.rooms = size;
		room = new Room(0,size);
	}
	public Boolean isAvailable(int cap){
		for(int i=0;i<rooms;i++){
			if(room.capacity > cap)
				return true;
		}
		return false;
	}

	public void allot(Exam a){
		int cap = a.students.size();
		if(cap < rooms){
			room.exams.add(a);
			room.capacity -= cap;
			this.rooms -= cap;
			this.exams.add(a);
		}
		else{
			System.out.println("\n\n\n\n asfasfasf \n\n\n\n");
		}
	}	
/*
	public void delete(Exam a){
		int cap = a.students.size();
		for(int i = 0;i < rooms;i++){
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
*/
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

	public Exam(String a,int students){
		this.identifier = a;
		this.no_of_students = students;
	}

}
class Student{
	String id;
	List<Exam> exams = new ArrayList<Exam>();
	List<Integer> exams_indices = new ArrayList<Integer>();
	public Student(String a,List<Integer> b){
		this.id = a;
		this.exams_indices = b;
	}


}
class timetable{
	public static Boolean isadmissible(int a, List<Integer> indices){
		if(indices.size() == 0){
			return true;
		}
		if(indices.contains(a))	return false;
		if((a%16)%3 == 0){
			if(indices.contains(a+1))	return false;
		}
		else if((a%16)%3 == 1){
			if(indices.contains(a+1))	return false;
			if(indices.contains(a-1))	return false;
		}
		else{
				if(indices.contains(a-1))	return false;
		}
		return true;	
	}
	public static void main(String[] args){
		int num_periods = Integer.parseInt(args[0]);
		int room_size = Integer.parseInt(args[1]);
		int enrollments = 0;
		List<Exam> exams = new ArrayList<Exam>();
		Period[] periods = new Period[num_periods];
		for(int i = 0;i < num_periods;i++){
			periods[i] = new Period(i,(i%16)%3,room_size);
		}
		int exam_index = 0;
		for(int i = 0; i < num_periods;){
			Random rand = new Random();
			int a = rand.nextInt(room_size/5) + 1;
		//	System.out.println(i+" "+a);
			if(a > periods[i].rooms){
				i++;
			}
			if(i == num_periods)	break;
			Exam dummy = new Exam(Integer.toString(exam_index),a);
			exams.add(dummy);
			periods[i].exams.add(dummy);
			periods[i].rooms -= a;
			enrollments += a;
			exam_index++;
		}
		int[] exams_sizes = new int[exam_index];
		for(int i=0; i<exam_index; i++){
			exams_sizes[i] = exams.get(i).no_of_students;
		}
		System.out.println(exam_index+" exams have been scheduled and number of enrollments is " +enrollments);
		List<Student> students = new ArrayList<Student>();
		int x = 0,student_index = 1,e_index = 0;
		System.out.println("d");
		while(e_index<enrollments){
			List<Integer> indices = new ArrayList<Integer>();
			for(int i=0;i<6;i++){
				Random rand = new Random();
				int a = rand.nextInt(exam_index);
				x = 0;
				System.out.println(i+" "+student_index+" "+a+" "+exam_index+" "+enrollments);
				for(int k=0;k<exam_index;k++)	System.out.print(exams_sizes[k]+" ");
				while(exams_sizes[a] == 0 || !isadmissible(a,indices)){	
					x++;
					a++;
					a = a % exam_index;
					if(x == exam_index)	break;
				}
				System.out.println("etesad "+i+" "+student_index+" "+a+" "+x);
				if(x == exam_index){
					break;
				}
				exams_sizes[a]--;
				indices.add(a);
				e_index++;
			}
			if(indices.size() > 0){
				Student dummy_student = new Student(Integer.toString(student_index),indices);
				students.add(dummy_student);
				student_index++;
			}

		}
		System.out.println("e");
		
		try{
			BufferedWriter bw_students = new BufferedWriter(new FileWriter(args[2]));
			BufferedWriter bw_exams = new BufferedWriter(new FileWriter(args[3]));
			BufferedWriter bw_timetable = new BufferedWriter(new FileWriter(args[4]));
			for(int i=0;i<num_periods;i++){
				System.out.println("Period "+i+": ");
				bw_timetable.write("Period "+i+": \n");
				System.out.print("Room no 0:");
				bw_timetable.write("Room no 0:");
				int s = periods[i].exams.size();
				for(int j=0;j<s;j++){
					System.out.print(periods[i].exams.get(j).identifier+";");
					bw_timetable.write(periods[i].exams.get(j).identifier+";");
				}
				bw_timetable.write("\n");
				System.out.println();
			}
			for(int i =0;i<exam_index;i++){
				System.out.println(exams.get(i).identifier+" "+exams.get(i).no_of_students);
				bw_exams.write(exams.get(i).identifier+" "+exams.get(i).no_of_students);
				bw_exams.write("\n");
			}
			int s = students.size();
			for(int i=0;i<s;i++){
				Student k = students.get(i);
				int k_e = k.exams_indices.size();
				for(int j=0;j<k_e;j++){
					System.out.println(k.id+" "+k.exams_indices.get(j));
					bw_students.write(k.id+" "+k.exams_indices.get(j)+"\n");
				}
			}
			bw_exams.close();
			bw_timetable.close();
			bw_students.close();
			
		}catch(Exception e){e.printStackTrace();}

	}
}