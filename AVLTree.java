

import java.util.ArrayList;
import java.util.List;

//created by: Ruben Wolhandler  rubenw       342674983
//            Daniel Malash    danielmalash  208059113


/**
*
* AVLTree
*
* An implementation of a AVL Tree with
* distinct integer keys and info
*
*/

public class AVLTree {
 
		 
		private IAVLNode root;
		private int balance_counter;
		private IAVLNode min=null;
		private IAVLNode max = null;
		
		
		/**
	  	 * AVLTree constructor 
	  	 */
		public AVLTree() {
			root = null;
			balance_counter = 0;
			
		}
		/**
	  	 * AVLTree constructor for given root
	  	 * update min and max
	  	 * 
	  	 */
		public AVLTree(IAVLNode root) {
			 
			 if(root.getKey() == -1) {
				 this.root = null;
			 }
		 
			 else{
				 this.root = root;
				 this.root.setParent(null);
				 min = findMin();
				 max = findMax();
			 }
			}

	/**
	  * public boolean empty()
	  *
	  * returns true if and only if the tree is empty
	  *
	  */
	 public boolean empty() {
		  if (root == null) {
			  return true;
		  }
	   return false; 
	 }

	/**
	  * public String search(int k)
	  *
	  * returns the info of an item with key k if it exists in the tree
	  * otherwise, returns null
	  */
	 public String search(int k)
	 {
		IAVLNode node = root;
		if(node == null) {
			return null;
		}
		while(node.isRealNode()) {
			
			if(node.getKey() == k) {
				return node.getValue();
			}
			else if(node.getKey()<k) {
				node = node.getRight();
			}
			else{  //node.getKey()>k
				node = node.getLeft();
			}
		}
		return null;  
	 }

	 /**
	  * public int insert(int k, String i)
	  *
	  * inserts an item with key k and info i to the AVL tree.
	  * the tree must remain valid (keep its invariants).
	  * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	  * returns -1 if an item with key k already exists in the tree.
	  */

	  public int insert(int k, String i) {
		  
		   IAVLNode node = root;
		   balance_counter = 0;
		   
			//if tree is null
			if(this.root == null) {
			   this.root = new AVLNode(k,i);
			   this.root.setParent(null);
			   min = root; //update min;
			   max = root; //update max;
			   return 0;
		   }
		   
		   else {
			   if(search(k)!=null) {
				  
				   return -1;
					}
				node = searchForInsert(k);
				IAVLNode nodeInsert = new AVLNode(k,i);
				insertNode(node,nodeInsert);
				rebalancingAfterInsert(node,nodeInsert);
		   }	
		  return balance_counter;
	  }
	 
  
	  /**
		 * private void insertNode(IAVLNode node,IAVLNode nodeInsert )
		  *
		  * insert nodeInsert on the right side of node if key(nodeInsert)>key(node)
		  * insert nodeInsert on the left side of node if key(nodeInsert)<key(node)
		  * update min and max of the tree if necessary
		  */
	  
	  private void insertNode(IAVLNode node,IAVLNode nodeInsert ) {
		  
		  	if(nodeInsert.getKey()<min.getKey()) {//update min
				min = nodeInsert;
			}
			if(nodeInsert.getKey()>max.getKey()) {//update max
				max = nodeInsert;
			}
			 //insert the nodeInsert
			 if(nodeInsert.getKey()<node.getKey()) {
				   node.setLeft(nodeInsert);
				   nodeInsert.setParent(node);
			   		
			   }
			   else {
				   node.setRight(nodeInsert);
				   nodeInsert.setParent(node);
			   }
			 
			 
		 }
	  
	  /** 
		 * private void rebalancingAfterInsert(IAVLNode node,IAVLNode nodeInsert)
		 * @param node ,nodeInsert
		 * Balance the tree after insert
		 */
	  
	  private void rebalancingAfterInsert(IAVLNode node,IAVLNode nodeInsert) {
	      
		  while(node.getHeight() == nodeInsert.getHeight() && node !=null) {
			   
				IAVLNode otherChild;
				if(nodeInsert.getKey() < node.getKey()) {
					otherChild = node.getRight();
					if(nodeInsert.isRealNode()) {//special case of join 
						if((node.getHeight() - otherChild.getHeight() == 2) && (nodeInsert.getHeight() - nodeInsert.getLeft().getHeight() == 1) && (nodeInsert.getHeight() - nodeInsert.getRight().getHeight() == 1)) {
							rotationRightforInsert(node);
							promote(nodeInsert);
						}
					 }}
			     else {
					otherChild = node.getLeft();
					if(nodeInsert.isRealNode()) {//special case of join
						if((node.getHeight() - otherChild.getHeight() == 2) && (nodeInsert.getHeight() - nodeInsert.getLeft().getHeight() == 1) && (nodeInsert.getHeight() - nodeInsert.getRight().getHeight() == 1)) {
							rotationLeftforInsert(node);
							promote(nodeInsert);
						}
					}
				}
								
				   if(node.getHeight()-otherChild.getHeight() == 1) {//case 1
					  promoteForInsert(node);
					  nodeInsert = node;
					  node = node.getParent();
					  
					  if(node == null)
						  break;
				  }
				  
				  else if(nodeInsert.getKey() < node.getKey()) {
					  
					  if(nodeInsert.getHeight()-nodeInsert.getRight().getHeight()==2) {//case2.1
						 
						  rotationRightforInsert(node);
						  break;}
					  
					  else {//case3.1
						  LRforInsert(node,nodeInsert);
						  break;}
				  }
				   
				  else {
					  if(nodeInsert.getHeight()-nodeInsert.getLeft().getHeight()==2) {//case2.2
						  rotationLeftforInsert(node);
						  break;}
					  
					  else {//case3.2
						  RLforInsert(node,nodeInsert);
						  break;}
					  }
				}			
		  }
	  
	  
	  private void promoteForInsert(IAVLNode node) {

		  promote(node);
		  balance_counter+=1;
		  
	  }
	  
	  /**
		 * private void rotationLeftforInsert(IAVLNode node)
		 * @param node
		 * rotate the tree 
		 */
	  
	  private void rotationLeftforInsert(IAVLNode node) {

		  demote(node);
		  rotationLeft(node);
		  balance_counter ++;
		  
	  }
	  
	  /**
		 * private void rotationRightforInsert(IAVLNode node)
		 * @param node ,nodeInsert
		 * rotate the tree 
		 */
	  private void rotationRightforInsert(IAVLNode node) {
		  demote(node);
		  rotationRight(node);
		  balance_counter++;
		  
	  }
	  
		/**
		 * private void LRforInsert(IAVLNode node,IAVLNode nodeInsert)
		 * @param node ,nodeInsert
		 * rotate the tree Left and Right
		 */
	  private void LRforInsert(IAVLNode node,IAVLNode nodeInsert) {
		  demote(node);
		  demote(nodeInsert);
		  promote(nodeInsert.getRight());
		  rotationLeft(nodeInsert);
		  rotationRight(node);
		  balance_counter+=2;
		  
	  }
	  
	  /**
	   * private void RLforInsert(IAVLNode node,IAVLNode nodeInsert)
		 * @param node ,nodeInsert
		 * rotate the tree Left and Right
		 */
	  private void RLforInsert(IAVLNode node,IAVLNode nodeInsert) {
		  demote(node);
		  demote(nodeInsert);
		  promote(nodeInsert.getLeft());
		  rotationRight(nodeInsert);
		  rotationLeft(node);
		  balance_counter+=2;
	  }
	  
	  /**
		 * private IAVLNode searchForInsert(int k)
		 * @param k
		 * search the leaf to put the new node
		 * 
		 */
	  
	  private IAVLNode searchForInsert(int k) {
		  
		  IAVLNode node = root;
		  if(node == null) {
			  return null;
		  }
		  
		  while(node.isRealNode()) {
				if(node.getKey()<k) {
					node = node.getRight();
				}
				else{  //node.getKey() > k
					node = node.getLeft();
				}
				
				}
		  if(node.getParent()!= null) {
			   node = node.getParent();
			   }
		  
		return node;
		  
			 
	  }
	  
	  private void promote(IAVLNode y) {
		   y.setHeight(y.getHeight()+1);
	  }
	  
	  private void demote(IAVLNode y) {
		   y.setHeight(y.getHeight()-1);
	 }
	    
	  /**
	   	* private void rotationRight(IAVLNode y)
		 * @param y 
		 * rotate the tree 
		 */
	  private void rotationRight(IAVLNode y) {
		  
		   IAVLNode tmp = y.getLeft();
		   IAVLNode rightSub = tmp.getRight();
		   tmp.setRight(y);
		   tmp.setParent(y.getParent());
		   y.setParent(tmp);
		   y.setLeft(rightSub);
		   rightSub.setParent(y);
		   if(this.root == y) { 
			   this.root=tmp;
		   }
		   else {
			   if(tmp.getParent().getKey()<tmp.getKey()) {
				   tmp.getParent().setRight(tmp);
				   
			   }
			   else {
				   tmp.getParent().setLeft(tmp);
			   }
		   }
		   }
	  
	  /**
		 * private void rotationLeft(IAVLNode y)
		 * @param y 
		 * rotate the tree 
		 */
	  
	  private void rotationLeft(IAVLNode y) {

		   IAVLNode tmp = y.getRight();
		   IAVLNode leftSub = tmp.getLeft();
		   tmp.setLeft(y);
		   
		   tmp.setParent(y.getParent());
		   y.setParent(tmp);
		   y.setRight(leftSub);
		   leftSub.setParent(y);
		   if(this.root == y) { 
			   this.root=tmp;
		   }
		   else {
			   if(tmp.getParent().getKey()<tmp.getKey()) {
				   tmp.getParent().setRight(tmp);
				   
			   }
			   else {
				   tmp.getParent().setLeft(tmp);
			   }
		   }
		   }
	  
	  /**
		 * private boolean isLeaf(IAVLNode x)
		 * @param x
		 * return true if x is a leaf else return false
		 * 
		 */
	  private boolean isLeaf(IAVLNode x) {
		  if(x.getRight().isRealNode() || x.getLeft().isRealNode()) {
			  return false;
		  }
		  return true;
	  }
	 
	  /**
		 * private boolean isUnaryNode(IAVLNode x)
		 * @param x
		 * return true if x is a UnaryNode else return false
		 * 
		 */
	  
	  private boolean isUnaryNode(IAVLNode x) {
		  if(isLeaf(x)) {
			  return false;
		  }
		  if(!x.getRight().isRealNode() || !x.getLeft().isRealNode())
			  return true;
		 return false; 
	  }

	  /**
		 * public IAVLNode searchNode(int k)
		 * @param k
		 * return the node with the key k 
		 * return null if there is no node with k keys
		 * 
		 */
	  
	  public IAVLNode searchNode(int k)
	  {
	 	IAVLNode node = root;
	 	if(node == null) {
	 		return null;
	 	}
	 	while(node.isRealNode()) {
	 		
	 		if(node.getKey() == k) {
	 			return node;
	 		}
	 		else if(node.getKey()<k) {
	 			node = node.getRight();
	 		}
	 		else{  //node.getKey()>k
	 			node = node.getLeft();
	 		}
	 	}
	 	return null;  
	  }
	 /**
	  * public int delete(int k)
	  *
	  * deletes an item with key k from the binary tree, if it is there;
	  * the tree must remain valid (keep its invariants).
	  * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	  * returns -1 if an item with key k was not found in the tree.
	  */
	  public int delete(int k)
	  {
		  balance_counter=0;
		  
		  IAVLNode deleteNode = searchNode(k);
		  
		  //if the keys k is no in the tree
		  if(deleteNode==null) {
			  return -1;
		  }
		 
		  IAVLNode parent = deleteNode.getParent();
		  
		  if(isLeaf(deleteNode)|| isUnaryNode(deleteNode)) {
			  
			  //the deleted node is the root
			  if(deleteNode == this.root) {
				  
				  //deleteNode is a leaf
				  if(isLeaf(deleteNode)) {
					  this.root = null;
				  }
				  
				  //deleteNode is unaryNode
				  	else {
					  
					  if(deleteNode.getRight().isRealNode()) {
						  this.root = deleteNode.getRight();
						  this.root.setParent(null);
						  this.root.setHeight(0);
					  }
					  else {
						  this.root = deleteNode.getLeft();
						  this.root.setParent(null);
						  this.root.setHeight(0);
					  }
					  
				  }
			  }
			  
			  //the delete node is no a root
			  
			  else if(deleteNode.getKey()>parent.getKey()) {
				  
				  if(deleteNode.getRight().isRealNode()) {
					  
					  	deleteNode.getRight().setParent(parent);
				  		parent.setRight(deleteNode.getRight());
				  }
				  
				  else {
					  deleteNode.getLeft().setParent(parent);
				  		parent.setRight(deleteNode.getLeft());
				  }
			  }
			  
			  
			  
			  else {
				  if(deleteNode.getRight().isRealNode()) {
					  	deleteNode.getRight().setParent(parent);
				  		parent.setLeft(deleteNode.getRight());
				  }
				  
				  else {
					  deleteNode.getLeft().setParent(parent);
				  		parent.setLeft(deleteNode.getLeft());
				  }
				  
			  }
			  if(parent !=null)
				  rebalancingAfterDelete(parent);    
		  }
		  
		  //the deleteNode is no a leaf or a unaryNode
		  
		  else {
			  IAVLNode tmp = successor(deleteNode); // it's not leaf so successor!=null
			  delete(tmp.getKey());
			  
			  if(deleteNode.getParent()!=null && deleteNode.getParent().getKey()>deleteNode.getKey()) {
				  deleteNode.getParent().setLeft(tmp);
			  }
			  else if(deleteNode.getParent()!=null && deleteNode.getParent().getKey()<deleteNode.getKey()) {
				  deleteNode.getParent().setRight(tmp);
			  }
			  if(this.root==deleteNode) {
				  this.root = tmp;
			  }
			  
			  tmp.setParent(deleteNode.getParent());
			  tmp.setLeft(deleteNode.getLeft());
			  tmp.getLeft().setParent(tmp);
			  tmp.setRight(deleteNode.getRight());
			  tmp.getRight().setParent(tmp);
			  tmp.setHeight(deleteNode.getHeight());
			  
		  }
		  
		  if(k == min.getKey()) { //update min
			  min = findMin();  
		  }
		  if(k == max.getKey()) {//update max
			  max = findMax();  
		  }
		  
		  return balance_counter;
	  }

	  
	  /**
	   * private int diffRight(IAVLNode parent)
	  	* precondition: parent != null
	  	* returns difference height between parent and this right child
	  	*/
	  
	  private int diffRight(IAVLNode parent) {
		  return parent.getHeight()-parent.getRight().getHeight(); 
	  }
	  
	  /**
	   * private int diffLeft(IAVLNode parent)
	  	* precondition: parent != null
	  	* returns difference height between parent and this Left child
	  	*/
	  private int diffLeft(IAVLNode parent) {
		  return parent.getHeight()-parent.getLeft().getHeight(); 
	  }
	  
	  /**
		 * private void rebalancingAfterDelete(IAVLNode parent)
		 * @param parent
		 * Balance the tree after deletion
		 */
	  
	  private void rebalancingAfterDelete(IAVLNode parent) {
		  
		  IAVLNode nextParent;
		  
		  if(diffLeft(parent)==diffRight(parent) && diffLeft(parent)==2) {
			  nextParent = parent.getParent();
			  balance_counter++;
			  demote(parent);
			  
			  if(nextParent!=null) {
				  rebalancingAfterDelete(nextParent);
			  }
		  }
		  else if(diffLeft(parent)==3) {//after deletion of left child
			  IAVLNode node = parent.getRight();
			  
			  if(diffLeft(node)==1 && diffRight(node)==1) { //case2
				  demote(parent);
				  promote(node);
				  rotationLeft(parent);
				  balance_counter++;
			  }
			  
			  else if(diffLeft(node)==2) {//case3
				  nextParent = parent.getParent();
				  demote(parent);
				  demote(parent);
				  rotationLeft(parent);
				  balance_counter+=1;
				  
				  if(nextParent!=null) {
					  rebalancingAfterDelete(nextParent);
				  }
			  }
			  
			  else if(diffRight(node)==2) { //case4
				  nextParent = parent.getParent();
				  demote(node);
				  demote(parent);
				  demote(parent);
				  promote(node.getLeft());
				  rotationRight(node);
				  rotationLeft(parent);
				  balance_counter+=2;
				  
				  if(nextParent!=null) {
					  rebalancingAfterDelete(nextParent);
				  }
				  
			  }
		  }
		  else if(diffRight(parent)==3) {//after deletion of right child
			  IAVLNode node = parent.getLeft();
			  
			  if(diffRight(node)==1 && diffLeft(node)==1) { //case2
				  demote(parent);
				  promote(node);
				  rotationRight(parent);
				  balance_counter++;
			  }
			  
			  else if(diffRight(node)==2) { //case3
				  nextParent = parent.getParent();
				  demote(parent);
				  demote(parent);
				  rotationRight(parent);
				  balance_counter+=1;
				  
				  if(nextParent!=null) {
					  rebalancingAfterDelete(nextParent);
				  }
			  }
			  
			  else if(diffLeft(node)==2) { //case4
				  nextParent = parent.getParent();
				  demote(node);
				  demote(parent);
				  demote(parent);
				  promote(node.getRight());
				  rotationLeft(node);
				  rotationRight(parent);
				  balance_counter+=2;
				  
				  if(nextParent!=null) {
					  rebalancingAfterDelete(nextParent);
				  }
			  }
		  }
	  }
	  
	  /**
	   * public String min()
	   *
	   * Returns the info of the item with the smallest key in the tree,
	   * or null if the tree is empty
	   */
	  public String min()
	  {
		   if(this.empty()) {
			   return null;
		   }
		   return min.getValue();
	  }

	  /**
	   * public String max()
	   *
	   * Returns the info of the item with the largest key in the tree,
	   * or null if the tree is empty
	   */
	  public String max()
	  {
		   if(this.empty()) {
			   return null;
		   }
		   return max.getValue(); 
	  }

	 /**
	  * public int[] keysToArray()
	  *
	  * Returns a sorted array which contains all keys in the tree,
	  * or an empty array if the tree is empty.
	  */
	 public int[] keysToArray()
	 {
		 if(empty()) {
			 return new int[0];
			 
		 }
	       int[] arr = new int[size()];
	       IAVLNode node = min;
	       arr[0] = node.getKey();
	       
	       for(int i = 1; i < this.size();i++) {
	    	   node = successor(node);
	    	   arr[i] = node.getKey();
	       }
	            
	       return arr;
	 }

	 /**
	  * public String[] infoToArray()
	  *
	  * Returns an array which contains all info in the tree,
	  * sorted by their respective keys,
	  * or an empty array if the tree is empty.
	  */
	 public String[] infoToArray()
	 {
		 if(empty()) {
			 return new String[0];
			 
		 }
	       String[] arr = new String[size()];
	       IAVLNode node = min;
	       arr[0]=node.getValue();
	       for(int i = 1; i<this.size();i++) {
	    	   node = successor(node);
	    	   arr[i] = node.getValue();

	       } 
	       return arr;                 
	 }

	 /**
	  * IAVLNode successor(IAVLNode node)
	  *@param node
	  * Returns the successor of node or null if the node has no successor
	  */
	 private IAVLNode successor(IAVLNode node) {
		
		IAVLNode tmp=node;
		
		if(node.getRight().isRealNode()) {
			AVLTree t = new AVLTree();
			t.root = node.getRight();
			return t.findMin();
			}
		
		else {
			while(tmp.getParent() != null && tmp.getParent().getKey() < tmp.getKey()) {
				tmp = tmp.getParent();
			}
			if(tmp.getParent() == null) {
				return null;
			}
			if(tmp.getParent().getKey() > tmp.getKey()) {
				return tmp.getParent();
			}
		}
		return null;
	}


	  /**
		  * private IAVLNode findMin()
		  * Returns the node with the minimum key of the tree or null if the tree is empty
		  */
	 private IAVLNode findMin() {
		   if(this.empty()) {
			   return null;
		   }
		   IAVLNode node = root;
		   while(node.getLeft().getKey()!=-1) {
			   node=node.getLeft();
		   }
		return node;
	}
	  
	  /**
		  * private IAVLNode findMax()
		  * Returns the node with the maximum key of the tree or null if the tree is empty
		  */
	 private IAVLNode findMax() {
		  if(this.empty()) {
			   return null;
		   }
		  IAVLNode node = root;
		  while(node.getRight().getKey()!=-1) {
			  node=node.getRight();
		   }
		return node;
		}

	  /**
		 * private int SubTreeSize(IAVLNode root)
		  * @param root
		  * Returns the number of nodes in the subtree
		  */
	 private int SubTreeSize(IAVLNode root) {
		
		if(root == null) {
			return 0;
		}
		
		else if(!root.isRealNode()) {
			return 0;
		}
		else {
			return SubTreeSize(root.getLeft()) + 1 + SubTreeSize(root.getRight());
		}
	}
	/**
	   * public int size()
	   *
	   * Returns the number of nodes in the tree.
	   *
	   * precondition: none
	   * postcondition: none
	   */
	  public int size()
	  {
		  return SubTreeSize(this.root); 
	  }
	  
	    /**
	   * public int getRoot()
	   *
	   * Returns the root AVL node, or null if the tree is empty
	   *
	   * precondition: none
	   * postcondition: none
	   */
	  public IAVLNode getRoot()
	  {
		   return this.root;
	  }
	  
	    /**
	   * public string split(int x)
	   *
	   * splits the tree into 2 trees according to the key x. 
	   * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
		  * precondition: search(x) != null
	   * postcondition: none
	   */   
	  
	  public AVLTree[] split(int x)
	  {
		  AVLTree [] res = new AVLTree[2];
		  List<IAVLNode> parentsList = new ArrayList<IAVLNode>();
		  IAVLNode node = searchNode(x);
		  AVLTree little = new AVLTree(node.getLeft());
		  AVLTree big = new AVLTree(node.getRight());
		  
		  IAVLNode parent = node.getParent();
		  while(parent != null) {
			  parentsList.add(parent);
			  parent = parent.getParent();
		  }
		  for(IAVLNode p: parentsList) {
			  if(p.getKey()<x) {
				  little.join(p, new AVLTree(p.getLeft()));
			  }
			  else {
				  big.join(p,new AVLTree(p.getRight()));
			  }
			  
		  }
		  res[0]=little;
		  res[1]=big;
		  
		  return res;
		  
	  }

	  /**
	   * public join(IAVLNode x, AVLTree t)
	   *
	   * joins t and x with the tree. 	
	   * Returns the complexity of the operation (rank difference between the tree and t)
		  * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	   * postcondition: none
	   */   
	  public int join(IAVLNode x, AVLTree t)
	  {
		  AVLTree tmp = join(this,x,t);
		  
		  this.root = tmp.root;
		  
		  min = tmp.min; //update min
		  max = tmp.max; //update max
		  
		  //if the trees empty
		  if(this.root == null && t.root ==null) {
			  return 1;
		  }
		  if(this.root == null) {
			  return t.root.getHeight()+1;
		  }
		  if(t.root == null) {
			  return this.root.getHeight()+1;
		  }
		  int complexity = Math.abs(this.root.getHeight()-t.root.getHeight())+1;
		  return complexity; 
	  }
	  
	  /**
	   * private AVLTree join(AVLTree t1,IAVLNode x, AVLTree t2)
	   *
	   * returns the joins tree of t1 and t2 with x
	   * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	   * 
	   *
	   */
	  private AVLTree join(AVLTree t1,IAVLNode x, AVLTree t2){
		  
		  
		  if(x==null) {
			  if(t1.root == null && t2.root == null) {
				  return t1;
			  }
			  else if(t1.root == null) {
				  return t2;
			  }
			  else if(t2.root == null) {
				  return t1;
			  }
			  else {
				  if(t1.max.getKey()>t2.max.getKey()) {
					  x = t2.max;
					  t2.delete(t2.max.getKey());
				  }
				  else {
					  x=t1.max;
					  t1.delete(t1.max.getKey());
				  }
				  return join(t1,x,t2);
			  }
			  
		  }
		  
		  
		  //update the node x so that he has no children
		  IAVLNode fic1 = new AVLNode(-1,"");
		  IAVLNode fic2 = new AVLNode(-1,"");
		  x.setLeft(fic1);
		  fic1.setParent(x);
		  x.setRight(fic2);
		  fic2.setParent(x);
		  x.setHeight(0);
		  x.setParent(null);
		 
		 // if t1 and t2 empty
		  if(t1.root == null && t2.root == null) {
			  t1.root = x;
			  t1.min = x;
			  t1.max = x;
			  x.setParent(null);
			  return t1;
		  }
		  
		  //if t1 empty and t2 no empty
		  if(t1.root == null) {
			  t2.insertNode(t2.searchForInsert(x.getKey()), x);
			  t1.root=t2.root;
			  t1.min = t2.min;
			  t1.max = t2.max;
	          t1.rebalancingAfterInsert(x.getParent(),x);
			  return t1;
		  }
		  
		  // if t2 empty and t1 no empty
		  if(t2.root == null) {
			  t1.insertNode(t1.searchForInsert(x.getKey()), x);
	          t1.rebalancingAfterInsert(x.getParent(),x);
			  return t1;
		  }
		  //if keys(t1)<keys(t2)
		  
		  if(t1.root.getKey() < t2.root.getKey()) {
			  
			  t1.max = t2.max;
			  IAVLNode nodeT2  = t2.root;
			  IAVLNode nodeT1 = t1.root;
			  int newHeight;
			  
			  if(nodeT2.getHeight() >= nodeT1.getHeight()+2) {
				  
				  while(nodeT2.getHeight() > nodeT1.getHeight()+2) {
					  nodeT2  = nodeT2.getLeft();
				  }
				  //joins the trees with x
				  
				  IAVLNode b = nodeT2.getLeft();
				  nodeT2.setLeft(x);
				  x.setParent(nodeT2);
				  x.setRight(b);
				  b.setParent(x);
				  x.setLeft(nodeT1);
				  nodeT1.setParent(x);
				  newHeight = Math.max(nodeT1.getHeight(), b.getHeight())+1;
				  x.setHeight(newHeight);
				  t1.root = t2.root;
				  if(x.getHeight()==x.getParent().getHeight()) {
	                  t1.rebalancingAfterInsert(x.getParent(),x);
				  }	
				  
				  return t1;
			  }
			  
			  else if(nodeT2.getHeight() + 2 <= nodeT1.getHeight()) {
				  
				  while(nodeT2.getHeight()+2<nodeT1.getHeight()) {
					  nodeT1  = nodeT1.getRight();
				  }
				  
				//joins the trees with x
				  IAVLNode b = nodeT1.getRight();
				  nodeT1.setRight(x);
				  x.setParent(nodeT1);
				  x.setLeft(b);
				  b.setParent(x);
				  x.setRight(nodeT2);
				  newHeight = Math.max(b.getHeight(), nodeT2.getHeight())+1;
				  x.setHeight(newHeight);
				  nodeT2.setParent(x);
				  t2.root = t1.root;
				  if(x.getHeight()==x.getParent().getHeight()) {
	                  t2.rebalancingAfterInsert(x.getParent(),x);
				  }	
				  return t2;  
			  }
			  
			  else {//abs(t1.height()-t2.height())<=1
				  
				  //joins the trees with x
				  x.setLeft(nodeT1);
				  nodeT1.setParent(x);
				  x.setRight(nodeT2);
				  nodeT2.setParent(x);
				  newHeight = Math.max(nodeT1.getHeight(), nodeT2.getHeight())+1;
				  x.setHeight(newHeight);
				  t1.root=x;
				  
				  return t1;
			  	}
			  }
		return join(t2,x, t1);
		  
	  }
	 
		/**
		   * public interface IAVLNode
		   * ! Do not delete or modify this - otherwise all tests will fail !
		   */
		public interface IAVLNode{	
			public int getKey(); //returns node's key (for virtuval node return -1)
			public String getValue(); //returns node's value [info] (for virtuval node return null)
			public void setLeft(IAVLNode node); //sets left child
			public IAVLNode getLeft(); //returns left child (if there is no left child return null)
			public void setRight(IAVLNode node); //sets right child
			public IAVLNode getRight(); //returns right child (if there is no right child return null)
			public void setParent(IAVLNode node); //sets parent
			public IAVLNode getParent(); //returns the parent (if there is no parent return null)
			public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
	   	public void setHeight(int height); // sets the height of the node
	   	public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
		}

	  /**
	  * public class AVLNode
	  *
	  * If you wish to implement classes other than AVLTree
	  * (for example AVLNode), do it in this file, not in 
	  * another file.
	  * This class can and must be modified.
	  * (It must implement IAVLNode)
	  */
	 public class AVLNode implements IAVLNode{
		  
			private int key;
		  	private String value;
		  	private IAVLNode left;
		  	private IAVLNode right;
		  	private int height;
		  	private IAVLNode parent=null;
		  	
		  	/**
		  	 * AVLNode constructor
		  	 */
		  	public AVLNode(int key , String value) {
		  		
		  		this.key = key;
		  		if(this.key != -1) {
		  			this.left = new AVLNode(-1,null);
		  			this.right = new AVLNode(-1,null);
		  			this.left.setParent(this);
		  			this.right.setParent(this);
		  			this.height = 0;
		  			this.value = value;
		  			
		  			}
		  		else {
		  			this.right = null;
		  			this.left =null;
		  			this.height = -1;
		  			this.value = null;

		  			}
		  	}
		  	
			public int getKey()
			{
				if(this.isRealNode())
					return this.key;
				return -1;
			}
			public String getValue()
			{
				if(!this.isRealNode()) {
					return null;
				}
				
				return this.value; 
			}
			public void setLeft(IAVLNode node)
			{
				this.left = node;

		 
			}
			public IAVLNode getLeft()
			{
				if(!this.isRealNode()) {
					return null;
				}
				return this.left;
			}
			public void setRight(IAVLNode node)
			{
				this.right = node;


			}
			public IAVLNode getRight()
			{
				if(!this.isRealNode()) {
					return null;
				}
				return this.right;
			}
			public void setParent(IAVLNode node)
			{
				this.parent=node;
				
			}
			public IAVLNode getParent()
			{
				if(parent == null)
					return null;
				return this.parent; 
			}
			// Returns True if this is a non-virtual AVL node
			public boolean isRealNode()
			{
				if(this == null || this.key == -1) {
					return false;
				}
				return true;
			}
			public void setHeight(int height)
			{
				this.height = height;
	   	
			}
			public int getHeight()
			{
				return this.height;
			}
	   

	 }

	}
