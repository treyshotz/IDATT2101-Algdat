#include <iostream>
#include <list>
#include <fstream>
#include <sstream>


using namespace std;

class Graph {
    int nodeNum;
    list<int> *nodeArr;

public:


    explicit Graph(int size) {
        nodeNum = size;
        //Might be wrong to set nodeArr to nodenum size. Might have to set it edges instead.
        nodeArr = new list<int>[nodeNum];
    }


    void addNode(int pos, int neighbour) {
        nodeArr[pos].push_back(neighbour);
    }


    void listAll() {
        for(int i = 0; i < nodeNum; i++) {
            cout << "Place " << i << ":";
            for(int j : nodeArr[i]) {
                cout << " --> " << j;
            }
            cout << endl;
        }
    }

    Graph getTranspose() {
        Graph g(nodeNum);

        return g;
    }
};




int main() {

    int size;
    int edges;
    string str;
    ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig5/L7g6.txt");
        getline(file, str);
    istringstream iss(str);
    iss >> size;
    iss >> edges;
    Graph graph(size);

    int pos;
    int neighbour;
    for(int i = 0; i < edges; i++) {
        getline(file, str);
        istringstream iss2(str);
        iss2 >> pos;
        iss2 >> neighbour;
        graph.addNode(pos, neighbour);
    }

    graph.listAll();

    return 0;
}
