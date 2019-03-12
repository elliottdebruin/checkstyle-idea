import os.path as path
import csv
import requests
import matplotlib.pyplot as plt
import numpy as np

def update_responses_file(file_path):
  '''
  Gets the most recent content in the Google Sheet containing the feedback
  form's responses and writes it to "file_path"
  '''
  # The id of the google sheet that holds our feedback form's responses
  sheet_id = '1UbKDC5v8Um6HFpE1IxNxyeKPwB1DkcMKANuheFg4p3w'
  url = 'https://docs.google.com/spreadsheets/d/' + sheet_id + '/export?format=csv'
  with open(file_path, 'w') as out:
    content = str(requests.get(url).content)[2:-1]
    content = (content
      .replace('\\r', '')
      .replace('\\n', '\n')
      .replace('\\\'', '\''))
    out.write(content)

def list_for_index_and_method(rows, i, method):
  '''
  Returns the list of times it took to complete the task associated with index
  "i" by way of "method".
  '''
  assert i % 2 == 0
  return tuple(float(row[i + 1]) for row in rows[1:] if row[i] == method)

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
  gui_series = tuple(sum(times) / len(times) for times in ttc_dict['GUI'].values())
  xml_series = tuple(sum(times) / len(times) for times in ttc_dict['XML'].values())

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
    header: tuple(row[i] for row in rows[1:])
    for i, header in enumerate(rows[0])
  }

def pie_chart_for(
  series,
  title,
  axis_label='',
  exploded_label='GUI',
  out_dir='.',
  file_name=''
):
  '''
  Saves a pie chart of "series" with a title of "title" in a file named
  "file_name" in directory "out_dir"
  '''
  labels = tuple(set(series))
  sizes = tuple(series.count(label) for label in labels)
  index = next(i for i, label in enumerate(labels) if label == exploded_label)
  explode = tuple(0.1 if i == index else 0 for i, label in enumerate(labels))

  plt.pie(sizes, explode=explode, labels=labels, autopct='%1.1f%%')
  plt.title(title)
  if len(axis_label) > 0:
    plt.xlabel(axis_label)
  plt.savefig(path.join(out_dir, file_name + '.jpg'))
  plt.cla()

def get_gui_preference_series(columns):
  gui_votes = columns['How hard/easy it is to create a linter configuration with the GUI?']
  xml_votes = columns['How hard/easy it is to create a linter configuration with XML?']
  assert len(gui_votes) == len(xml_votes)

  return tuple('GUI'  if int(gui_votes[i]) > int(xml_votes[i]) else 'XML' for i in range(len(gui_votes)))

def method_difficulty_bar_chart(
  answers,
  title='',
  xlabel='Difficulty (1 low, 5 high)',
  ylabel='Number of votes',
  out_dir='.',
  file_name=''
):
  labels = tuple(range(1, 6))
  series = tuple(answers.count(str(label)) for label in labels)
  
  plt.bar(labels, series)
  plt.xlabel(xlabel)
  plt.ylabel(ylabel)
  plt.title(title)

  plt.savefig(path.join(out_dir, file_name + '.jpg'))
  plt.cla()

def main():
  # File names
  time_to_complete_file_name = 'time_to_complete.csv'
  feedback_responses_file_name = 'feedback_responses.csv'
  directory, _ = path.split(__file__)

  # Get most recent CSV from form responses
  update_responses_file(path.join(directory, feedback_responses_file_name))

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
        exploded_label='I don\'t know',
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
        out_dir=path.join(directory, 'figures'),
        file_name='preferred_method_' + str(i + 1)
      )
    series = get_gui_preference_series(fr_dict)
    pie_chart_for(
      series,
      'Which method is easier overall for creating a linter configuration?',
      axis_label=(
        'Number who preferred GUI: '
        + str(series.count('GUI'))
        + ', Number who preferred XML: '
        + str(series.count('XML'))
      ),
      out_dir=path.join(directory, 'figures'),
      file_name='preferred_method_overall'
    )
    for question in (
      'How hard/easy it is to create a linter configuration with the GUI?',
      'How hard/easy it is to create a linter configuration with XML?'
    ):
      method_difficulty_bar_chart(
        fr_dict[question],
        question,
        out_dir=path.join(directory, 'figures'),
        file_name='difficulty_' + question[-4:-1].lower()
      )

main()