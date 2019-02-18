/*
We want to maintain the list of employees in a company. We will be concerned with two quantities associated with each 
employee in the company -- name of the employee (you can assume no two employees in the company have the same name), 
and the level of the employee. The level denotes where the person stands in the hierarchy. Level 1 denotes the highest
 post in the company (say the CEO), level 2 comes below level 1 and so on. There is only 1 person at level 1, but there 
 can be several employees at level i > 1. Each level i employee works under a level i-1 employee, which is his/her 
 immediate boss. Given an employee A, we can form a sequence of employees A',A'', A''', ... where A works under A', A' 
 works under A'', and so on. We say that each employee in A',A'', A''',... is a boss of A. We would like to implement 
 a suitable tree data-structure so that we can implement the following operations : 
 
• AddEmployee(S,S_) : We want to add a new employee whose name is S. This employee will work under an existing employee S_ 
(note that this automatically decides the level of S, it is one more than that of S_). 
• DeleteEmployee(S,S_) : We want to remove an employee whose name is S. S_ is the name of another employee in the
 company who is at the same level as S. When we remove S, all the persons working under S will now work under S_. 
 • LowestCommonBoss(S,S_) : S and S_ are names of two persons working in the company. This operation prints the name
 of the employee A who is a boss of both S and S_, and among all such persons has the largest level. In other words, 
 we want to find the common boss who is lowest in the hierarchy in the company. 
 • Print Employees : This operation prints the name of the employees in the company. It should print the names according 
 to the levels of the employees. So first it should print the person level 1, then people at level 2, and so on.
 
Write a program which implements such a data-structure. Credit will be given to choice of proper datastructures and 
efficiency. For example, in the second operation above, one should not just search the entire tree to look for the 
name of the employee S_. Your program should also catch errors, for example if in the first operation above, there is 
no employee with name S_, then it should say it is an error. Use the notions of exceptions in JAVA to implement error checking.  
 
Input format: first we specify the initial set of employees as follows:  
Num_Employees  
EmployeeName1     BossName1  
EmployeeName1     BossName1  
....  
 
After this the file will contain a sequence of operations specified as follows 
(call the above operations 0,1,2,3 in the order they are described above):  
Num_Commands  
Query_type1 Employee1 Employee2 (where Query_Type1 is 0,1,2,3. Note that if Query_type is 3, then no other arguments are needed). 
*/
import java.util.*; 
import java.io.*; 

class tree
{
	node root;
	bst bstree;
	public tree()
	{
		root=new node();
		bstree=new bst();
	}
	public tree(String S_)
	{
		root=new node(S_);
		bstree=new bst(root);
	}
	
	public void AddEmployee(String S, String S_)
	{
		try
		{
			node temp=new node();
			temp=bstree.search(S_, bstree.bst_root).nodeMainTree;
			node temp2=new node();
			temp2=temp.addchild(S);
			bstree.add(temp2, bstree.bst_root);
		}
		catch(CustomException f)
		{
			System.out.println(f);
			System.exit(0);
		}

	}
	public void DeleteEmployee(String S, String S_) throws CustomException
	{
		node temp1;
		node temp2;
		temp1=bstree.search(S, bstree.bst_root).nodeMainTree;
		temp2=bstree.search(S_, bstree.bst_root).nodeMainTree;
		if(temp1.level!=temp2.level)
		{
			throw new CustomException("Level Mismatch");
		}
		else if(S.equals(S_))
		{
			throw new CustomException("Args can't be equal");
		}
		else
		{
			Vector<node> s_temp;
			s_temp=temp1.children;
			temp2.addchildren(s_temp);
			bstree.delete(temp1.name, bstree.bst_root);
			delete(temp1);
		}
	}
	public String LowestCommonBoss(String S,String S_)
	{
		try
		{
			node temp1;
			node temp2;
			temp1=bstree.search(S, bstree.bst_root).nodeMainTree;
			temp2=bstree.search(S_, bstree.bst_root).nodeMainTree;
			
			while(temp1.level>temp2.level)
			{
				temp1=temp1.parent;
			}	
			while(temp1.level<temp2.level)
			{
				temp2=temp2.parent;
			}
			boolean c=true;
			while(c)
			{
				if(temp1.parent.name.equals(temp2.parent.name))
				{
					c=false;
					return temp1.parent.name;
				}
				else
				{
					temp1=temp1.parent;
					temp2=temp2.parent;
				}
			}	
		}
		catch(CustomException f)
		{
			System.out.println(f);
			System.exit(0);
		}
		return " ";

	}
	public Queue<String> PrintEmployees()
	{
		Queue<node> q = new LinkedList<>();
		Queue<node> q_out = new LinkedList<>();
		q.add(root);
		while(q.size()>0)
		{
			node temp;
			temp=q.remove();
			q_out.add(temp.name);
			for(int i=0; i<temp.children.size(); i++)
			{
				q.add(temp.children.get(i));
			}
		}
		return q_out;
	}
	void delete(node temp)
	{
		node p=temp.parent;
		p.children.remove(temp);
	}
	
}
class bst
{
	public node_bst bst_root;

	public bst()
	{
		bst_root=new node_bst();
	}
	
	public bst(node n)
	{
		bst_root = new node_bst(n.name, n);
	}
		
	
	public void add(node N, node_bst root) throws CustomException
	{
		
		if(N.name.compareTo(root.name)>0)
		{
			if(root.right==null)
			{
				root.right=new node_bst(); 
				root.right.name=N.name;
				root.right.nodeMainTree=N;
			}
			else
			{
				add(N,root.right);
			}
		}
		else if(N.name.compareTo(root.name)<0)
		{
			if(root.left==null)
			{
				root.left=new node_bst();
				root.left.name=N.name;
				root.left.nodeMainTree=N;
			}
			else
			{
				add(N,root.left);
			}
		}
		else
		{
			throw new CustomException("Name already exists");
		}	
	
	}
	public node_bst search(String S, node_bst root) throws CustomException
	{		
		if(S.compareTo(root.name)>0)
		{
			if(root.right==null)
			{
				throw new CustomException("Name not found");
			}
			return search(S,root.right);
		}
		else if(S.compareTo(root.name)<0)
		{
			if(root.left==null)
			{
				throw new CustomException("Name not found");
			}
			return search(S,root.left);
		}
		else
		{
			return root;
		}
		
	}
	
	public node_bst delete(String S, node_bst root)
	{
		if(root==null) 
		{	
			return root;
		}
		if(S.compareTo(root.name)<0) 
		{
		    root.left=delete(S, root.left);
		} 
		else if(S.compareTo(root.name)>0) 
		{
        	    root.right=delete(S, root.right);
		} 
		else 
		{
            		if(root.left==null && root.right==null)
			{
               			 return null;
            		} 
			else if(root.left==null) 
			{
                		return root.right;
            		} 
			else if(root.right==null) 
			{
                	return root.left;
            		} 
			else 
			{
				node_bst temp=minNode(root.right);
                		root.name=temp.name;
				root.nodeMainTree=temp.nodeMainTree;
                		root.right=delete(temp.name, root.right);
            		}
        	}
 
        	return root;	
	}
	node_bst minNode(node_bst root)
	{
		if(root.left==null)
		{
			return root;
		}
		else
		{
			return minNode(root.left);
		}
	}
	
}
class node
{
	public node parent;
	public String name;
	public int level;
	public Vector<node> children;
	
	public node()
	{
		parent=null;
		name=null;
		children=new Vector<node>();
	}
	
	public node(String string)
	{
		name = string;
		level = 1;
		children = new Vector<node>();
	}
	
	public node(String string, node n)
	{
		this(string);
		parent = n;
		level = n.level + 1;
	}
	
	
	public node addchild(String S)
	{
		node p=new node();
		p.name=S;
		p.parent=this;
		p.level=this.level+1;
		this.children.add(p);
		return p;
	}
	public void addchildren(Vector<node> s)
	{
		for(int i=0; i<s.size();i++)
		{
			this.children.add(s.get(i));
			s.get(i).parent=this;
		}
	}
}
class CustomException extends Exception
{
	String error;
	public CustomException(String message)
	{
		error=message;
	}
	public String toString()
	{
		return error; 
	}
}
class node_bst
{
	public node_bst left;
	public node_bst right;
	public String name;
	public node nodeMainTree;
	
	
	//Constructors
	public node_bst()
	{
		name=null;
		left=null;
		right=null;
		nodeMainTree=null;
	}
	public node_bst(String string, node n)
	{
		name = string;
		nodeMainTree = n;
	}
}

public class assignment2
{
	public static void main(String args[])
	{
		try 
		{ 
			FileInputStream fstream = new FileInputStream("simpletestinput.txt");
			Scanner s = new Scanner(fstream);
			
			PrintWriter pw = new PrintWriter("out.txt");
			pw.close();
				
			FileOutputStream fs = new FileOutputStream("out.txt",true); 
			PrintStream p = new PrintStream(fs);
			
			int n=Integer.parseInt(s.nextLine());
			String[] st=s.nextLine().split(" ");
			tree employees=new tree(st[1]);
			employees.AddEmployee(st[0], st[1]);	
			for(int i=0; i<n-2; i++)
			{
				String[] str=s.nextLine().split(" ");
				employees.AddEmployee(str[0], str[1]);
			}
			n=Integer.parseInt(s.nextLine());
			for(int i=0; i<n; i++)
			{
				String[] stri=s.nextLine().split(" ");
				int op=Integer.parseInt(stri[0]);
				if(op==0)
				{
					employees.AddEmployee(stri[1],stri[2]);
				}
				else if(op==1)
				{
					employees.DeleteEmployee(stri[1],stri[2]);
				}
				else if(op==2)
				{
					p.println(employees.LowestCommonBoss(stri[1],stri[2]));
				}	
				else
				{
					Queue<String> q_out = new LinkedList<>();
					q_out=employees.PrintEmployees();
					while(q_out.size()>0)
					{
						p.println(q_out.remove());
					}
				}
			}
			
		} 
		catch (FileNotFoundException e) 
		{ 
			System.out.println("File not found");
		} 
		catch(CustomException f)
		{
			System.out.println(f);
		}
		
	}
}
