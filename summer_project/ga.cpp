#include <iostream>
#include <fstream>
#include <vector>
#include <unordered_map>
//#include <stdc++>
#include <algorithm>		//random
#include <cstdlib>
#include <bitset>
using namespace std;

int pop_size = 6;

unordered_map<string, vector<string>> umap, e_students;
unordered_map<string, int> exam_cap;
unordered_map<string, int> exam_map;
vector<string> arr[16][33][12];
int room_arr[16];


int m_penalty(string e1, int tt, int p){
	//int mut_penalty;
	vector<string> mp;
	for(int i = 0; i < 16; i++ ){
		for(vector<string>::iterator it = arr[i][p][tt].begin(); it != arr[i][p][tt].end(); it++){
			string e2 = *it;
			unordered_map<string, vector<string>>::const_iterator it1 = e_students.find(e1);
			unordered_map<string, vector<string>>::const_iterator it2 = e_students.find(e2);
			vector<string> st1 = it1->second;
			vector<string> st2 = it2->second;
			sort(st1.begin(), st1.end());
			sort(st2.begin(), st2.end());
			set_intersection(st1.begin(), st1.end(), st2.begin(), st2.end(), back_inserter(mp));
		}
	}
	return mp.size();
}

int av_penalty(int tt){
	int p = 0;
	for(int i = 0; i < 31; i++ ){
		vector<string> pen;
		for(int j = 0; j < 16; j++){
			for(vector<string>::iterator it = arr[j][i][tt].begin(); it != arr[j][i][tt].end(); it++){
				string e1 = *it;
				for(int j = 0 ; j < 16; j++){
					for(vector<string>::iterator it2 = arr[j][i+1][tt].begin(); it2 != arr[j][i+1][tt].end(); it2++){
						string e2 = *it2;
						
						unordered_map<string, vector<string>>::const_iterator it1 = e_students.find(e1);
						unordered_map<string, vector<string>>::const_iterator it3 = e_students.find(e2);
						vector<string> st1 = it1->second;
						vector<string> st2 = it3->second;
						
						sort(st1.begin(), st1.end());
						sort(st2.begin(), st2.end());
						set_intersection(st1.begin(), st1.end(), st2.begin(), st2.end(), back_inserter(pen));
					}
				}
			}
		}
		p += pen.size();
	}
	return p/32;
}

int Siz(int tt, int p){
	int size = 0;
	for(int i = 0; i < 16; i++){
		size += arr[i][p][tt].size();
	}
	return size;
}

int penalty(string event, int tt, int period){

	//int p = 0;			p -> penalty
	vector<string> p;

	if(period > 0){			//checking previous
		
		for(int i = 0; i < 16; i++){
			for(vector<string>::iterator it = arr[i][period-1][tt].begin(); it != arr[i][period-1][tt].end(); it++){
				string e = *it;

				unordered_map<string, vector<string>>::const_iterator it1 = e_students.find(e);
				unordered_map<string, vector<string>>::const_iterator it2 = e_students.find(event);
				vector<string> st1 = it1->second;
				vector<string> st2 = it2->second;

				sort(st1.begin(), st1.end());
				sort(st2.begin(), st2.end());
				set_intersection(st1.begin(), st1.end(), st2.begin(), st2.end(), back_inserter(p));
			}
		}
	}
	int n = 32;

	if(period < n-1){		//checking next
		
		for(int i = 0; i < 16; i++){
			for(vector<string>::iterator it = arr[i][period-1][tt].begin(); it != arr[i][period-1][tt].end(); it++){
				string e = *it;
				unordered_map<string, vector<string>>::const_iterator it1 = e_students.find(e);
				unordered_map<string, vector<string>>::const_iterator it2 = e_students.find(event);
				vector<string> st1 = it1->second;
				vector<string> st2 = it2->second;

				sort(st1.begin(), st1.end());
				sort(st2.begin(), st2.end());
				set_intersection(st1.begin(), st1.end(), st2.begin(), st2.end(), back_inserter(p));
			}
		}
	}

	return p.size();
}

bool intersect(string e1, string e2){

	unordered_map<string, vector<string>>::const_iterator it1 = e_students.find(e1);
	unordered_map<string, vector<string>>::const_iterator it2 = e_students.find(e2);
	vector<string> st1 = it1->second;
	vector<string> st2 = it2->second;

	sort(st1.begin(), st1.end());
	sort(st2.begin(), st2.end());
	bool first = false;
	int length;
	vector<string> *lower, *higher;

	if(st1.size() < st2.size()){
		length = st1.size();
		lower = &st2;
		higher = &st1;
	}
	else{
		length = st2.size();
		lower = &st1;
		higher = &st2;
	}
	for (int i = 0; i < length; ++i)
	{
		bool found = binary_search(lower->begin(), lower->end(), higher->at(i));
		if(found)
			return true;
		else return false;
	}
}

int legal_period(string event, int tt, int period){
	bool legal = false;

	int room = -1;										//room -> room in which exam is placed 

	//num_of sitting <= rooms available
	for(int i = 0; i < 16; i++){
		int sum_of_rcap = 0;
		for(vector<string>::iterator it = arr[i][period][tt].begin(); it != arr[i][period][tt].end(); ++it){
			string exam = *it;

			unordered_map<string, int>::const_iterator cons_it = exam_cap.find(event);
			sum_of_rcap += cons_it->second;
			if(intersect(exam, event))
				return -1;
		}
		unordered_map<string, int>::const_iterator cons_it = exam_cap.find(event);
		sum_of_rcap += cons_it->second;
		
		if(room == -1 && sum_of_rcap <= room_arr[i]){
			room = i;
		}
		//else return false;
	}

	return room;
	//arr[room][period][tt].push_back(event)
}

int main(int argc, char *argv[]){

//-----------------------------------------------------------------------
//READING DATA FROM FILE
//	unordered_map<string, vector<string>> umap, e_students;
//	unordered_map<string, int> exam_cap;


	ifstream file_enrol, file_exam, file_roomCap, ex_period, ex_room;			//constraints -> ex_period, ex_room
	file_enrol.open(argv[1]);

	string student_code, exam_code;

	while(!file_enrol.eof()){ 

		file_enrol >> student_code >> exam_code;
		cout<<student_code<<"	"<<exam_code<<"\n";				//1st input enrolements
		umap[student_code].push_back(exam_code);
		e_students[exam_code].push_back(student_code);
		exam_cap[exam_code]++;
	}

	file_enrol.close();



//	unordered_map<string, int> exam_map;

	file_exam.open(argv[2]);
	
	string ex_code;//, ex_time;
	int ex_time;
	string ex_arr[805];
	int counter = 0; 
	
	while(!file_exam.eof()){
		file_exam >> ex_code >> ex_time;							//2nd input exams
		exam_map[ex_code] = ex_time;
		ex_arr[counter] = ex_code;
		counter++;
	}
	file_exam.close();



	file_roomCap.open(argv[3]);
//	int room_arr[16];
	int room, cap;

	while(!file_roomCap.eof()){
		file_roomCap >> room >> cap;
		room_arr[room] = cap;
	}
	file_roomCap.close();


	ex_period.open(argv[4]);
	unordered_map<string, int> ex_pMap;
	string ex_p_code;
	int ex_p_per;			//ex_p_code -> in case(exam_period file) -> code of exam
	while(!ex_period.eof()){
		ex_period >> ex_p_code >> ex_p_per;
		ex_pMap[ex_p_code] = ex_p_per;
	}
	ex_period.close();


	ex_room.open(argv[5]);
	unordered_map<string, int> ex_rMap;
	string ex_r_code;
	int ex_r_room;
	while(!ex_room.eof()){
		ex_room >> ex_r_code >> ex_r_room;
		ex_rMap[ex_r_code] = ex_r_room;
	}
	ex_room.close();


//--------------------------------------------------------------------------
//INITIAL POPULATION

//	int pop_size = 6;
//	vector<string> arr[16][33][pop_size];					//16 rooms 32 periods + 1 unscheduled
	

	for (int i = 0; i < pop_size; ++i){

		for (int q = 0; q < 805; ++q){				//all exams put in unscheduled category
			arr[0][32][i].push_back(ex_arr[i]);	
			arr[0][32][i + pop_size].push_back(ex_arr[i + pop_size]);	
		}

		random_shuffle ( arr[0][32][i].begin(), arr[0][32][i].end() );
		random_shuffle ( arr[0][32][i + 1].begin(), arr[0][32][i + 1].end() );

		for ( auto local_it = ex_pMap.begin(); local_it!= ex_pMap.end(); ++local_it ){
      		int p = local_it -> second;
      		for(int j = 0; j < 16 ; j++ ){
      			string e = local_it -> first;
      			if(legal_period(e, i, p) != -1){
      				arr[j][p][i].push_back(local_it -> first);
      				arr[j][p][i + 1].push_back(local_it -> first);
      			}
      		}
		}

		for ( auto local_it = ex_rMap.begin(); local_it!= ex_rMap.end(); ++local_it ){
      		int r = local_it -> second;
      		for (int j = 0; j < 33; ++j){

      		 	string e = local_it -> first;
      		 	if(legal_period(e, i, j) != -1){
      		 		arr[r][j][i].push_back(local_it -> first);
      		 		arr[r][j][i + 1].push_back(local_it -> first);
      		 	}
      		 } 
		}

		for(vector<string>::iterator it = arr[0][32][i].begin(); it != arr[0][32][i].end(); ++it){

			string event = arr[0][32][i].back();
			arr[0][32][i].pop_back();
			arr[0][32][i + 1].pop_back();
		

			if (ex_pMap.find(event) == ex_pMap.end() || ex_rMap.find(event) == ex_rMap.end()){	//if no constraints the exam scheduled then
				int goodness = 0;					//16 rooms 32 periods + 1 unscheduled
	
				for (int j = 0; j < 32; ++j){
					int numCommon = legal_period(event , i, j);
					if(numCommon != -1){
						goodness += (numCommon + Siz(i, j) + 1) / (penalty(event, i, j) + 1);
					}
				}

				int good_arr[goodness], counter = 0;

				for (int j = 0; j < 32; ++j){
					int numCommon = legal_period(event , i, j);
					if(numCommon != -1){
						int good_level = (numCommon + Siz(i, j) + 1) / (penalty(event, i, j) + 1);
						int num_pos = good_level;
						//float pos = good_level/goodness*(1.0);
						/*if(num_pos + 0.5 < pos){
							num_pos += 1;
						}*/
						while(num_pos){
							good_arr[counter] = j;
							counter++;
							num_pos--;
						}
					}
				}
				int random_per = rand() % goodness;
				int assign_period = good_arr[ random_per ];
				int room = legal_period(event, i, assign_period);
				arr[room][assign_period][i];
				arr[room][assign_period][i + pop_size];

			}
		}	
	}


	//-------------------------------------------------------------------------------
	//Mutation + hybrid local search

	int num_of_generations = 10;
	
	while(num_of_generations--){

		int roulette_arr[10000];

		int type_mut = rand() % 2;
		pop_size = 6;

		for (int i = 0; i < pop_size; ++i)
		{
			i += pop_size;
			if(type_mut == 0){						//light mutation
				
			}
			else if(type_mut == 1){					//heavy mutation
				bitset<32> disrupt;
				int average = av_penalty(i);
				for (int q = 0; q < 31; ++q) 			//for every period(except the last) in the timetable
				{
					int per_penalty = 0;
					for(int z = 0 ; z < 16 ; z++ ){		//for every room in that period
						//arr[z][q][i].
						for(vector<string>::iterator it = arr[z][q][i].begin(); it != arr[z][q][i].end(); ++it){		//for every exam
							
							string e = *it;
							int mut_penalty = m_penalty(e, i, q);		//e -> exam check its intersection with exams in priod q+1
							per_penalty += mut_penalty;

						}						
					}

					if(per_penalty < average){
						disrupt[q] = 0;
					}
					else {
						if(q >= 1 && disrupt[q-1] == 0){
							disrupt[q] = 0;
							disrupt[q+1] = 1;
						}
						else{
							disrupt[q] = 1;
						}
					}
				}

				vector<string> rearrange;
				for (int q = 0; q < 32; ++q){
					
					if(disrupt[q] == 1){
						for (int z = 0; z < 16; ++z)
						{
							rearrange.push_back(arr[q][z][i].back());
							arr[q][z][i].pop_back();	
						}
					}	
					random_shuffle ( rearrange.begin(), rearrange.end() );
				}

				for(vector<string>::iterator it = rearrange.begin(); it != rearrange.end(); ++it){
					
					bool found_p = false;
					string exam = *it;

					for(int q = 0 ; q < 32; q++){
						
						int room = legal_period(exam, i, q); 
						if(room != -1){
							arr[room][q][i].push_back(exam);
						}
					}
					if(found_p == false){
						arr[0][32][i].push_back(exam);
					}
				}

				for(vector<string>::iterator it = arr[0][32][i].begin(); it != arr[0][32][i].end(); ++it){
					bool found_p = false;
					string exam = *it;
					for(int q = 0 ; q < 32; q++){
						int room = legal_period(exam, i, q); 
						if(room!= -1){
							arr[room][q][i].push_back(exam);
							it = arr[0][32][i].erase(it);			//removinf it from the unscheduled ones
						}
					}
				}
			}


			//---------------------------------------------------------
			//Hill climbing operator

			
			for(int j = 0 ; j < 32 ;j++){
				for(int k = 0 ; k < 16; k++){
					for(vector<string>::iterator it = arr[k][j][i].begin(); it != arr[k][j][i].end(); it++){
						string e = *it;
						int p = penalty(e, i, j);
						int new_period = -1;
						for (int l = 0; l < 32; ++l){
							if(l == j)
								continue;
							else {
								int new_p = penalty(e, l, i);
								if(p > new_p){
									p = new_p;
									new_period = l;
								}
							}
						}

						if(new_period != -1){
							int new_r = legal_period(e, new_period, i);
							arr[new_r][new_period][i].push_back(e);
							arr[k][j][i].erase(it);
						}
					}
				}
			}
			i -= pop_size;

		}
	
		//now we have twice the population size
		//----------------------------------------------------------
		//SELECTION

		long int tot_fitness = 0;

		int unsched = arr[0][32][i].size();
		for (int i = 0; i < 2*pop_size; ++i)
		{
			int num_conflicts = av_penalty(i) * 32;
			int loc_fitness = (10*805) / (200*unsched + num_conflicts);						//num_events = 805
			tot_fitness += loc_fitness;
		}

		int roulette_counter = 0;

		for (int i = 0; i < 2*pop_size; ++i)
		{
			int num_conflicts = av_penalty(i) * 32;
			int loc_fitness = (10*805) / (200*unsched + num_conflicts);						//num_events = 805
			int area_in_roulette = (loc_fitness / tot_fitness) * 10000;
			
			for (int j = 0; j < area_in_roulette; ++j){
				roulette_arr[roulette_counter] = i;
				roulette_counter++;
			} 	
		}

		//Again make the new array

		int new_c = 0;
		for (int i = 0; i <  pop_size; ++i)
		{
			int random_num = rand() % pop_size;
			int rand_tt = roulette_arr[random_num]; 

			for(int i = 0; i < 33; i++){
				for(int j = 0; j < 16; j++){
					arr[j][i][counter] = arr[j][i][random_num];					
				}
			}
		}
	}
}