def type(text, delay = 0.07)
  <<-BEGIN
set textBuffer to "#{text}"
repeat with i from 1 to count characters of textBuffer
  keystroke (character i of textBuffer)
  delay #{delay}
end repeat
  BEGIN
end

def clear()
  "keystroke \"k\" using command down"
end

def save(commands) 
  output = commands.join("\n")
  File.open("script.scpt", 'w') do  |file|
    file << output
  end
end

lines = File.open(ARGV[0], "r").read.split("\n")


commands = lines.map do |line|
  if line.include?("{")
    command = send(line.match(/{(.*)}/)[1])
  else
    command = type(line)
  end
<<-BEGIN
 tell application "System Events" to tell process "Terminal"
  delay 2 
  #{command}
  keystroke return
 end tell
BEGIN
end

save(commands)
