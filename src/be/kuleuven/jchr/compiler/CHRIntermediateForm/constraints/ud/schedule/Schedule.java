package be.kuleuven.jchr.compiler.CHRIntermediateForm.constraints.ud.schedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Schedule implements ISchedule {
    private List<IScheduleComponent> components;
    
    public Schedule() {
        setComponents(new ArrayList<IScheduleComponent>());
    }
    
    public Iterator<IScheduleComponent> iterator() {
        return getComponents().iterator();
    }

    protected List<IScheduleComponent> getComponents() {
        return components;
    }
    protected void setComponents(List<IScheduleComponent> components) {
        this.components = components;
    }
    
    public void addComponent(IScheduleComponent scheduleComponent) {
        getComponents().add(scheduleComponent);
    }
    public <T extends IScheduleComponent> void addComponents(Collection<T> scheduleComponents) {
        getComponents().addAll(scheduleComponents);
    }
    public void addComponentAt(IScheduleComponent scheduleComponent, int index) {
        getComponents().add(index, scheduleComponent);
    }
    
    @Override
    public String toString() {
        return getComponents().toString();
    }
}
