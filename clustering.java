/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.*;
 

public class clustering {
    public static void main(String [] args){
		if (args.length < 3){
		    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
		    return;
		}
		try{
		    BufferedImage originalImage = ImageIO.read(new File(args[0]));
		    int k=Integer.parseInt(args[1]);
		    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
		    ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 
		    
		}catch(IOException e){
		    System.out.println(e.getMessage());
		}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
		int w=originalImage.getWidth();
		int h=originalImage.getHeight();
		BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
		Graphics2D g = kmeansImage.createGraphics();
		g.drawImage(originalImage, 0, 0, w,h , null);
		// Read rgb values from the image
		int[] rgb=new int[w*h];
		int count=0;
		for(int i=0;i<w;i++){
		    for(int j=0;j<h;j++){
		    	rgb[count++]=kmeansImage.getRGB(i,j);
		    }
		}
		// Call kmeans algorithm: update the rgb values
		kmeans(rgb,k);
	
		// Write the new rgb values to the image
		count=0;
		for(int i=0;i<w;i++){
		    for(int j=0;j<h;j++){
		    	kmeansImage.setRGB(i,j,rgb[count++]);
		    }
		}
		return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k){
    	int[] c = new int[k];
    	
    	int[] a = new int[rgb.length];
    	
    	int[] mean = new int[k];
    	
    	int[] count = new int[k];
    	
    	long mismatch = 0;
    	int prev_assignment;
    	
	   	Random random = new Random();
    	for(int i=0;i<k;i++){
    		c[i] = random.nextInt() * -1;    		
    	}
    	
    	while(true){
	    	mismatch = 0;
	    	for(int i=0;i<rgb.length;i++){
	    		prev_assignment = a[i];
	    		a[i] = min_dist(rgb[i], c);
	    		
	    		if(prev_assignment != a[i]){
	    			mismatch += 1;
	    		}
	    	}
	    	
	    	c = find_mean(a, rgb, k);
	    	
	    	if(mismatch == 0){
	    		break;
	    	}
    	}
    	    	
    	for(int i=0;i<rgb.length;i++){
    		rgb[i] = c[a[i]];
    		
    		count[a[i]]++;
    	}
    	
    }
    
    private static int min_dist(int x_i, int c[]){
    	double dist, min_dist=0;
    	int min_i=0;
    	
    	for(int i=0;i<c.length;i++){
    		dist = Math.pow((x_i - c[i]), 2);
    		
    		if(i==0){
    			min_dist = dist;
    			min_i = i;
    		} else {
    			if(dist < min_dist){
    				min_dist = dist;
    				min_i = i;
    			}
    		}
    	}
    	
    	return min_i;
    }
    
    private static int[] find_mean(int[] a, int[] rgb, int k){
    	double[] total_sum = new double[k];
    	double[] freq_count = new double[k];
    	
    	int[] mean = new int[k];
    	
    	for(int i=0;i<k;i++){
    		total_sum[i] = 0;
    		freq_count[i] = 0;
    	}
    	
    	for(int i=0;i<rgb.length;i++){
    		total_sum[(int)a[i]] += rgb[i];
    		freq_count[(int)a[i]] += 1;
    	}
    	    	
    	for(int i=0;i<k;i++){
    		mean[i] = (int)(total_sum[i] / freq_count[i]);
    	}
    	
    	return mean;
    }
    

}
