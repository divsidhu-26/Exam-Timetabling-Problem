import sys
import numpy as np
import matplotlib.pyplot as plt

with open(sys.argv[1]) as f:
    data = f.read()

data = data.split('\n')

x = [row.split(' ')[0] for row in data]
y = [row.split(' ') for row in data]

print x
print y

# plt.plot(x,y,'b')
# plt.xlabel('K')
# plt.ylabel('Broadcast time')
# plt.title('1a MeanTime vs K ')
# plt.show()

