package dev.code.workshop.tools.action;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaskManagementTools {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong taskIdGenerator = new AtomicLong(1);

    @Tool(description = "Create a new task with title, description, and assignee")
    public TaskResult createTask(String title, String description, String assignee) {
        Long tasktId = taskIdGenerator.getAndIncrement();
        Task task = new Task(tasktId, title, description, assignee, TaskStatus.PENDING);
        tasks.put(tasktId, task);


        //In real implementation: save to database, send notification email
        return new TaskResult(tasktId, title, "PENDING", assignee, "Task created successfully and assigned to " + assignee);
    }

    @Tool(description = "Update task status by task ID")
    public TaskResult updateStatus(Long taskId, TaskStatus status) {
        Task existingTask = tasks.get(taskId);
        if (existingTask == null) {
            return new TaskResult(taskId, "", "ERROR", "", "Task not found");
        }

        Task updatedTask = new Task(existingTask.id(), existingTask.title(), existingTask.description(), existingTask.assignee(), status);
        tasks.put(taskId, updatedTask);

        //In real implementation: update database, trigger workflow, send notification
        return new TaskResult(taskId, updatedTask.title(), status.toString(), updatedTask.assignee(), "Task status update to " + status);
    }

    @Tool(description = "Assing or reassign a task to a different person")
    public TaskResult assingTask(Long taskId, String newAssignee) {
        Task existingTask = tasks.get(taskId);
        if (existingTask == null) {
            return new TaskResult(taskId, "", "ERROR", "", "Task not found");
        }

        Task updatedTask = new Task(existingTask.id(), existingTask.title(), existingTask.description(), newAssignee, existingTask.status());
        tasks.put(taskId, updatedTask);

        //In real implementation: update database, send notification to new assignee
        return new TaskResult(taskId, updatedTask.title(), updatedTask.status().toString(), newAssignee, "Task reassigned to " + newAssignee);
    }
}
