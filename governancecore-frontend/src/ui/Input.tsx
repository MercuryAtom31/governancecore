import "./Input.css";

// This is a Input primitive UI which will be reused throughout the project.

type Props = React.InputHTMLAttributes<HTMLInputElement>;

export default function Input(props: Props) {
  return <input className="ui-input" {...props} />;
}