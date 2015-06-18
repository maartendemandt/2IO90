import os
import subprocess

list = os.listdir('.');
list.reverse()
commands = [];
commands.append("C:\\python34\\python.exe " + os.getcwd() + "/%s/clear_io.py")
commands.append("C:\\python34\\python.exe " + os.getcwd() + "/%s/generate_all.py")
commands.append("C:\\python34\\python.exe " + os.getcwd() + "/%s/test_all.py")
for x in list:
  if os.path.isdir(x):
    if x.endswith("eil3"):
      for cmd in commands:
        proc = subprocess.Popen(cmd % x, cwd = os.getcwd() + "\\" + x);
        proc.wait();
      i = input("Check the results! (press enter if you are done)")
      if i == 'quit':
        break
	
input("Press Enter to continue...")