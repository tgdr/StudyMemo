package buu.njj.studymemo.ui;

public class HirschbergComparision 
{
  
    public HirschbergComparision()
    {
    	
    }
    
    public String hirschbergAlgorithm(StringDescription original, StringDescription comparing)
    {
    	if (comparing.content == null || comparing.end < comparing.begin || original.end < original.begin)
    	{
    		return "";
    	}
    	else if (comparing.end == comparing.begin)
    	{
    		String s ="";
    		
    		for (int i = original.begin; i <= original.end; i++)
    		{
    			if (original.content.charAt(i) == comparing.content.charAt(comparing.begin))
    			{
    				s += original.content.charAt(i); 
    			}
    			
    			
    		}
    		
    		return s;
    	}
    	else if (original.begin == original.end)
    	{
    		String s ="";
    		
    		for (int i = comparing.begin; i <= comparing.end; i++)
    		{
    			
    			if (comparing.content.charAt(i) == original.content.charAt(original.begin))
    			{
    				s += comparing.content.charAt(i); 
    			}
    		}
    		return s;
    	}
    	
    	
    	int mid = (original.end + original.begin) / 2;
    	int originalEnd = original.end;
    	int originalBegin = original.begin;
    	original.end = mid;
    	int[] mcsFirstHalf = computeMCSLength(original, comparing, false);
    	
    	original.begin = mid + 1;
    	original.end = originalEnd;
    	int[] mcsSecondHalf = computeMCSLength(original, comparing, true);
    	
    	int maxPos = 0;
    	int comparingLength = comparing.end - comparing.begin + 1;
    	int maxVal = 0;
    	for (int j = 0; j < comparingLength; j++)
    	{
    		if (mcsFirstHalf[j] + mcsSecondHalf[comparingLength - j -1] > maxVal)
    		{
    			maxPos = j;
    			maxVal = mcsFirstHalf[j] + mcsSecondHalf[comparingLength - j -1];
    		}
    	}
    	
    
    	
    	int comparingEnd = comparing.end;
    	int comparingBegin = comparing.begin;
    	
    	original.begin = originalBegin;
    	original.end = mid;
    	comparing.end = comparing.begin + maxPos ;
    	String first = hirschbergAlgorithm(original, comparing);
    	
    	original.begin = mid + 1;
    	original.end = originalEnd;
    	comparing.begin = comparingBegin + maxPos + 1;
    	comparing.end = comparingEnd;
    	String second = hirschbergAlgorithm(original, comparing);
    	
    	return first  + second;
    	
    }
    
    private int[] computeMCSLength(StringDescription original, StringDescription comparing, boolean reverse)
    {
    	int comparingContentLength = comparing.end - comparing.begin + 1;
    	int originalContentLength = original.end - original.begin + 1;
    	
    	int[][] mcsLength = new int[2][comparingContentLength];
    	
    	for (int i = 0; i < comparingContentLength; i++)
    	{
    		mcsLength[1][i] = 0;
    	}
    	
    	if (original.end < original.begin || comparing.end < comparing.begin)
    	{
    		return mcsLength[1];
    	}
    	
    	String strOriginal = original.content.substring(original.begin, original.end + 1);
    	String strComparing = comparing.content.substring(comparing.begin, comparing.end + 1);
    	if (reverse)
    	{
    		strOriginal = new StringBuilder(strOriginal).reverse().toString();
    		strComparing = new StringBuilder(strComparing).reverse().toString();
    	}
    	
    	for (int i = 0; i < originalContentLength; i++)
    	{
    		for (int k = 0; k < comparingContentLength; k++)
    		{
    			mcsLength[0][k] = mcsLength[1][k];
    		}
    		
    		for (int j = 0; j < comparingContentLength; j++)
    		{
    			if (strOriginal.charAt(i) == strComparing.charAt(j))
    			{
    				mcsLength[1][j] = j > 0 ? mcsLength[0][j - 1] + 1 : 1;
    			}
    			else
    			{
    				if (j > 0)
    				{
    					mcsLength[1][j] = Math.max(mcsLength[1][j-1], mcsLength[0][j]);	
    				}
    				else
    				{
    					mcsLength[1][j] = 0;
    				}
    				
    			}
    		}
    	}
    	
    	return mcsLength[1];
    }
}
