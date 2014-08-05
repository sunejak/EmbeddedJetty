package tokenserver;

class SimpleThread extends Thread {
	
	int number = 0;
    int index = 0;
	long should_end_by = 0;
	
    public SimpleThread(String str, int ind, int num) {
	super(str);
	number = num;
    index = ind;
    }
    public void run() {
    	
    	should_end_by = System.currentTimeMillis() + 5000;
    	
    new Invoke_HTTP_Get().action(getName(), 4, number, index);
   /* 	
    	
	for (int i = 0; i < 10; i++) {
		
		
		
	    System.out.println(i + " " + getName());
            try {
		sleep((int)(Math.random() * 1000));
	    } catch (InterruptedException e) {}
	}
	System.out.println("DONE! " + getName());
    
    */
    }
}
