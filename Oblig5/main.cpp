#include <iostream>
#include <list>
#include <fstream>
#include <sstream>


using namespace std;

class Graph {
    int nodeNum;
    list<int> *nodeArr;

public:

    /*
     * Constructor for creating linked list of nodes
     */
    explicit Graph(int size) {
        nodeNum = size;
        nodeArr = new list<int>[nodeNum];
    }

    /*
     * Adds edge to linked list by pushing the new node at the back
     */
    void addEdge(int pos, int neighbour) {
        nodeArr[pos].push_back(neighbour);
    }

    /*
     * Lists all nodes and edges for all the nodes
     */
    void listAll() {
        for(int i = 0; i < nodeNum; i++) {
            cout << "Place " << i << ":";
            for(int j : nodeArr[i]) {
                cout << " --> " << j;
            }
            cout << endl;
        }
    }

    /*
     * Transposes the whole graph by using iterator and
     * "turning" the direction of every neighbour to a given node
     * the other way around
     */
    Graph getTranspose() {
        Graph transposedGraph(nodeNum);
        for(int i = 0; i < nodeNum; i++) {
            list<int>::iterator j;
            for(j = nodeArr[i].begin(); j != nodeArr[i].end(); j++) {
                transposedGraph.nodeArr[*j].push_back(i);
            }
        }
        return transposedGraph;
    }

    /*
     * Runs a recursive DFS search on graph.
     * Uses iterator to go through graph/node-array
     * Since this is the last used DFS this prints
     * out nodes during runtime to show SCC
     */
    void DFS(int pos, bool visited[]) {
        visited[pos] = true;
        cout << pos << " ";

        list<int>::iterator i;
        for(i = nodeArr[pos].begin(); i != nodeArr[pos].end(); i++) {
            if(!visited[*i]) {
                DFS(*i, visited);
            }
        }
    }

    /*
     * Same as DFS but this also fills the stack
     */
    void stackFill(int pos, bool visited[], stack<int> &stack) {
        visited[pos] = true;

        list<int>::iterator i;
        for(i = nodeArr[pos].begin(); i != nodeArr[pos].end(); i++) {
            if(!visited[*i]) {
                stackFill(*i, visited, stack);
            }
        }
        //Add the node to the stack. Only difference from DFS method....
        stack.push(pos);
    }

    /**
     * Finds SCC based on Kosaraju's algorithm which includes
     * DFS, traversing and then DFS again.
     */
    void findStronglyConnected() {
        stack<int> Stack;

        bool *visited = new bool[nodeNum];
        for(int i = 0; i < nodeNum; i++) {
            visited[i] = false;
        }

        //Uses stackfill DFS to add nodes to stack
        for(int i = 0; i < nodeNum; i++) {
            if(!visited[i]) {
                stackFill(i, visited, Stack);
            }
        }

        Graph transposedGraph = getTranspose();

        for(int i = 0; i < nodeNum; i++) {
            visited[i] = false;
        }

        int count = 1;
        while(!Stack.empty()) {
            int pos = Stack.top();
            if(!visited[pos]) {
                cout << "SCC " << count << ": ";
                count++;
                transposedGraph.DFS(pos, visited);
                //To make a new line after DFS have printed nodes
                cout << endl;
            }
            Stack.pop();
        }

    }
};


int main() {

    //Setting opp nodes and edges
    int size;
    int edges;
    string str;
    //Clion needs absolute path to read from file...
    ifstream file("/Users/madslun/Documents/Programmering/AlgDat/Oblig5/L7Skandinavia.txt");
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
        graph.addEdge(pos, neighbour);
    }

    //graph.listAll();

    graph.findStronglyConnected();

    return 0;
}
