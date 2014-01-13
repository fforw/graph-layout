package org.oseditor.graph;

public class PriorityQueue
{
    private int count;
    private int[] priorities;
    private int[] values;

    public PriorityQueue(int maxCapacity)
    {
        priorities = new int[maxCapacity];
        values = new int[maxCapacity];
        count = 0;
    }

    public void add(int priority, int value)
    {
        int index = findInsertIndex(priority, 0, count);

        if (index < count)
        {
            System.arraycopy(priorities, index, priorities, index + 1, count - index);
            System.arraycopy(values, index, values, index + 1, count - index);
        }
        priorities[index] = priority;
        values[index] = value;

        count++;
    }

    private int findInsertIndex(int priority, int lo, int hi)
    {
        while (lo < hi)
        {
            int mid = (lo + hi) >> 1;

            int p = this.priorities[mid];
            if (priority == p)
            {
                return  mid;
            }
            else
            {
                if (priority < p)
                {
                    hi = mid;
                }
                else
                {
                    lo = mid + 1;
                }
            }
        }

        return lo;
    }

    private int findIndexWithValue(int priority, int value, int lo, int hi)
    {
        while (lo < hi)
        {
            int mid = (lo + hi) >> 1;

            int p = this.priorities[mid];
            if (priority == p)
            {
                if (values[mid] == value)
                {
                    return mid;
                }
                else
                {
                    lo = mid-1;
                    while (lo >= 0 && priorities[lo] == p)
                    {
                        if (values[lo] == value)
                        {
                            return lo;
                        }
                        lo--;
                    }

                    hi = mid+1;
                    while (hi < count && priorities[hi] == p)
                    {
                        if (values[hi] == value)
                        {
                            return hi;
                        }
                        hi++;
                    }
                    break;
                }
            }
            else
            {
                if (priority < p)
                {
                    hi = mid;
                }
                else
                {
                    lo = mid + 1;
                }
            }
        }

        return -1;
    }

    public void lowerPriority(int value, int priority, int lowerPriority)
    {

        if (lowerPriority >= priority)
        {
            throw new PriorityNotLowerException(priority, lowerPriority);
        }

        int oldIndex = findIndexWithValue(priority, value, 0, count);

        if (oldIndex < 0)
        {
            throw new QueueElementNotFound(priority, value);
        }

        int newIndex = findInsertIndex(lowerPriority, 0, oldIndex);

        System.arraycopy(priorities, newIndex, priorities, newIndex + 1, oldIndex - newIndex);
        System.arraycopy(    values, newIndex,     values, newIndex + 1, oldIndex - newIndex);

        priorities[newIndex] = lowerPriority;
        values[newIndex] = value;
    }

    public int getValue(int index)
    {
        return values[index];
    }

    public int getPriority(int index)
    {
        return priorities[index];
    }

    public int size()
    {
        return count;
    }

    public int popMinimalValue()
    {
        if (count == 0)
        {
            throw new QueueEmptyException();
        }

        int value = values[0];
        count--;
        System.arraycopy(    values, 1,     values, 0, count);
        System.arraycopy(priorities, 1, priorities, 0, count);

        return  value;
    }

    public boolean isEmpty()
    {
        return count == 0;
    }
}
