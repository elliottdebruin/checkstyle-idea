import os
import os.path as path
import csv
import matplotlib.pyplot as plt
import numpy as np

def list_for_index_and_method(rows, i, method):
  '''
  Returns the list of times it took to complete the task associated with index
  "i" by way of "method".
  '''
  assert i % 2 == 0
  return [float(row[i + 1]) for row in rows[1:] if row[i] == method]

def time_to_complete_dict(rows):
  '''
  Returns a dictionary mapping methods to dictionaries mapping tasks to a list of
  their completion times by the aforemoentioned method.
  '''
  headers = rows[0]
  indexes = {
    task: next(i for i, header in enumerate(headers) if header[:6] == task)
    for task in ['Task 1', 'Task 2', 'Task 3']
  }
  return {
    method: {
      task: list_for_index_and_method(rows, indexes[task], method)
      for task in indexes.keys()
    }
    for method in ['GUI', 'XML']
  }

def plot_time_to_complete(ttc_dict, out_dir='.'):
  '''
  Saves a bar chart of the average completion time for each task in directory
  "out_dir".
  '''
  # Data to plot
  n_groups = 3
  gui_series = [sum(times) / len(times) for times in ttc_dict['GUI'].values()]
  xml_series = [sum(times) / len(times) for times in ttc_dict['XML'].values()]

  # Create plot
  index = np.arange(n_groups)
  bar_width = 0.25

  series1 = plt.bar(index, gui_series, bar_width, color='b', label='GUI')
  series2 = plt.bar(index + bar_width, xml_series, bar_width, color='r', label='XML')

  plt.ylabel('Average Completion Time (s)')
  plt.title('Average Completion by Task and Method')
  plt.xticks(index + (bar_width / 2), ('Task 1', 'Task 2', 'Task 3'))
  plt.legend()

  plt.savefig(path.join(out_dir, 'average_completion_time.jpg'))
  plt.cla()

def column_dict(rows):
  '''
  Converts a CSV list-of-lists to a mapping from column header to a list of the
  values in said column.
  '''
  return {
    header: [row[i] for row in rows[1:]]
    for i, header in enumerate(rows[0])
  }

def pie_chart_for(series, title, exploded_label='I don\'t know', out_dir='.', file_name=''):
  '''
  Saves a pie chart of "series" with a title of "title" in a file named
  "file_name" in directory "out_dir"
  '''
  labels = list(set(series))
  sizes = [series.count(label) for label in labels]
  index = next(i for i, label in enumerate(labels) if label == exploded_label)
  explode = [0.1 if i == index else 0 for i, label in enumerate(labels)]

  plt.pie(sizes, explode=explode, labels=labels, autopct='%1.1f%%')
  plt.title(title)
  plt.savefig(path.join(out_dir, file_name + '.jpg'))
  plt.cla()

# File names
time_to_complete_file_name = 'time_to_complete.csv'
feedback_responses_file_name = 'feedback_responses.csv'
directory, _ = path.split(__file__)

# Lines of CSV
with open(directory + '/' + time_to_complete_file_name) as ttc_file:
  ttc = list(csv.reader(ttc_file))
  plot_time_to_complete(time_to_complete_dict(ttc), path.join(directory, 'figures'))

with open(directory + '/' + feedback_responses_file_name) as fr_file:
  fr = list(csv.reader(fr_file))
  fr_dict = column_dict(fr)
  for i, question in enumerate([
    'What is the parent modules of "LineLength"?',
    'What is the parent modules of "AvoidStarImport"?',
    'What is the parent modules of "Indentation"?'
  ]):
    pie_chart_for(
      fr_dict[question],
      question,
      out_dir=path.join(directory, 'figures'),
      file_name='parent_module_' + str(i + 1)
    )
  for i, question in enumerate([
    'In Task 1, would you rather do it with the GUI tool  or the XML?',
    'In Task 2, would you rather do it with the GUI tool  or the XML?',
    'In Task 3, would you rather do it with the GUI tool  or the XML?'
  ]):
    pie_chart_for(
      fr_dict[question],
      question,
      exploded_label='GUI',
      out_dir=path.join(directory, 'figures'),
      file_name='preferred_method_' + str(i + 1)
    )